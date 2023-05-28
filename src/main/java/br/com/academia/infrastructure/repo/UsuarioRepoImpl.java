package br.com.academia.infrastructure.repo;


import br.com.academia.application.repo.UsuarioRepo;
import br.com.academia.application.usecase.exception.TokenInvalido;
import br.com.academia.application.usecase.exception.UsuarioExiste;
import br.com.academia.application.usecase.exception.UsuarioNaoExiste;
import br.com.academia.dto.Pair;
import br.com.academia.dto.UsuarioDto;
import br.com.academia.infrastructure.service.CriptografiaService;
import br.com.academia.infrastructure.service.TokenJwtService;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;

@ApplicationScoped
public class UsuarioRepoImpl implements UsuarioRepo {

    @Inject
    TokenJwtService jwtService;

    @Inject
    DSLContext db;


    private static final String QUERY_VALIDAR_INFORMACOES_DE_ACESSO =
            "SELECT COUNT(*) AS quantidade "
                    + "FROM usuario "
                    + "WHERE usuario_email = ? "
                    + "AND usuario_senha = ? ";

    private static final String QUERY_VERIFICAR_EXISTENCIA_USUARIO =
            "SELECT COUNT(*) AS quantidade "
                    + "FROM usuario "
                    + "WHERE usuario_email = ? ";
    private static final String QUERY_OBTER_INFORMACOES_USUARIO =
            "SELECT "
                    + "usuario_id AS id, "
                    + "usuario_nome AS nome, "
                    + "usuario_email AS email "
                    + "FROM usuario "
                    + "WHERE usuario_email = ? "
                    + "AND usuario_senha = ? ";

    private static final String QUERY_CRIAR_NOVO_USUARIO =
            "INSERT INTO usuario ( "
                    + "usuario_nome, "
                    + "usuario_email, "
                    + "usuario_senha "
                    + ") VALUES (?, ?, ?) ";

    private static final String QUERY_ADICIONAR_TOKEN_ALTERACAO =
            "INSERT INTO token_alteracao ( "
                    + "token_alteracao_usuario, "
                    + "token_alteracao_confirmacao, "
                    + "token_alteracao_data_confirmacao, "
                    + "token_alteracao_final, "
                    + "token_alteracao_data_final "
                    + ") VALUES ( "
                    + "(SELECT usuario_id FROM usuario WHERE usuario_email = ? LIMIT 1), "
                    + "?, "
                    + "DATE_ADD(NOW(), INTERVAL 2 HOUR), "
                    + "?, "
                    + "DATE_ADD(NOW(), INTERVAL 4 HOUR) "
                    + ") ON DUPLICATE KEY UPDATE "
                    + "token_alteracao_confirmacao = VALUES(token_alteracao_confirmacao), "
                    + "token_alteracao_data_confirmacao = DATE_ADD(NOW(), INTERVAL 2 HOUR), "
                    + "token_alteracao_final = VALUES(token_alteracao_final), "
                    + "token_alteracao_data_final = DATE_ADD(NOW(), INTERVAL 4 HOUR) ";

    private static final String QUERY_VALIDAR_TOKEN_ALTERACAO_SENHA =
            "SELECT "
                    + "COUNT(*) AS quantidade "
                    + "FROM token_alteracao "
                    + "WHERE token_alteracao_usuario = (SELECT usuario_id FROM usuario WHERE usuario_email = ?) "
                    + "AND token_alteracao_confirmacao < Date(NOW()) "
                    + "AND token_alteracao_confirmacao = ? ";

    private static final String QUERY_OBTER_TOKEN_CONFIRMACAO_FINAL =
            "SELECT "
                    + "token_alteracao_final AS token "
                    + "FROM token_alteracao "
                    + "WHERE token_alteracao_usuario = (SELECT usuario_id FROM usuario WHERE usuario_email = ?) ";

    private static final String QUERY_VERIFICAR_TOKEN_FINAL =
            "SELECT "
                    + "COUNT(*) AS quantidade "
                    + "FROM token_alteracao "
                    + "WHERE token_alteracao_final  = ? "
                    + "AND token_alteracao_usuario = (SELECT usuario_id FROM usuario WHERE usuario_email = ?) "
                    + "AND token_alteracao_data_final  > DATE(NOW()) ";

    private static final String QUERY_ALTERACAO_SENHA =
            "UPDATE usuario  SET "
                    + "usuario_senha = ? "
                    + "WHERE usuario_email  = ? ";

    private static final String QUERY_OBTER_USUARIO_TOKEN =
            "SELECT "
                    + "usuario_id AS id, "
                    + "usuario_nome AS nome, "
                    + "usuario_sobrenome AS sobrenome "
                    + "FROM usuario "
                    + "WHERE usuario_email = ? ";

    @Override
    public void validarInformacoes(String email, String senha) {
        Log.info("[UsuarioRepoImpl] Iniciando a verificacao da existencia de um usuario com email " + email + "...");
        Result<Record> quantidadeAsResult = db.fetch(QUERY_VALIDAR_INFORMACOES_DE_ACESSO, email, senha);
        if (quantidadeAsResult.get(0).get("quantidade", Integer.class) == 0) throw new UsuarioNaoExiste();
        Log.info("[UsuarioRepoImpl] Verificacao da existencia de um usuario com email " + email + " realizado com sucesso!");
    }

    @Override
    public UsuarioDto autenticarUsuario(String email, String senha) {
        Log.info("[UsuarioRepoImpl] Iniciando a autenticacao do usuario com email " + email + "...");
        Record usuarioAsRecord = db.fetch(QUERY_OBTER_INFORMACOES_USUARIO, email, senha).get(0);
        UsuarioDto usuario = criarUsuarioDeResult(usuarioAsRecord);
        Log.info("[UsuarioRepoImpl] Iniciando criacao dos token de acesso para o usuario com email " + email);
        Pair<String, String> tokens = jwtService.gerarTokenAutenticacao(usuario.nome, email, usuarioAsRecord.get("id", Integer.class));
        usuario.adicionarTokens(tokens);
        Log.info("[UsuarioRepoImpl] Autenticacao do usuario com email " + email + " realizado com sucesso!");
        return usuario;
    }

    @Override
    public void verificarExistencia(String email) {
        Log.info("[UsuarioRepoImpl] Iniciando verificacao se o email " + email + " esta livre para uso...");
        Record quantidadeAsRecord = db.fetch(QUERY_VERIFICAR_EXISTENCIA_USUARIO, email).get(0);
        if (quantidadeAsRecord.get("quantidade", Integer.class) > 0) throw new UsuarioExiste();
        Log.info("[UsuarioRepoImpl] Verificacao se o email " + email + " esta livre para uso realizado com sucesso!");
    }

    @Override
    @Transactional
    public void criarNovoUsuario(String nome, String email, String senha) {
        Log.info("[UsuarioRepoImpl] Iniciando a criacao de um novo usuario para o email " + email + "...");
        db.execute(QUERY_CRIAR_NOVO_USUARIO, nome, email, senha);
        Log.info("[UsuarioRepoImpl] Criacao de um novo usuario para o email " + email + " realizado com sucesso!");
    }

    @Override
    @Transactional
    public void solicitarAlteracaoSenha(String email) {
        Log.info("[UsuarioRepoImpl] Iniciando solicitacao da alteracao da senha do email " + email + "...");
        String tokenConfirmacao = CriptografiaService.gerarTokenNumerico();
        String tokenAlteracao = CriptografiaService.gerarTokenNumerico();
        db.execute(QUERY_ADICIONAR_TOKEN_ALTERACAO, email, tokenConfirmacao, tokenAlteracao);
        Log.info("[UsuarioRepoImpl] Solicitacao da alteracao da senha do email " + email + " realizado com sucesso");
        // TODO enviar email
    }

    @Override
    public void validarTokenAlteracaoSenha(String email, String token) {
        Log.info("[UsuarioRepoImpl] Iniciando validacao do token de alteracao de senha do email " + email + "...");
        Record quantidadeAsRecord = db.fetch(QUERY_VALIDAR_TOKEN_ALTERACAO_SENHA, email, token).get(0);
        if (quantidadeAsRecord.get("quantidade", Integer.class) == 0) throw new TokenInvalido();
        Log.info("[UsuarioRepoImpl] Validacao do token de alteracao de senha do email " + email + " realizado com sucesso!");
    }

    @Override
    @Transactional
    public void alterarSenha(String email, String senha, String token) {
        Log.info("[UsuarioRepoImpl] Iniciando alteracao da senha do email " + email + "...");
        db.execute(QUERY_ALTERACAO_SENHA, senha, email);
        Log.info("[UsuarioRepoImpl] Alteracao da senha do email " + email + " realizado com sucesso!");
    }

    @Override
    public void verificarSeUsuarioNaoExiste(String email) {
        Log.info("[UsuarioRepoImpl] Iniciando a verificacao da existencia de um usuario com email " + email + "...");
        Integer quantidade = db.fetch(QUERY_VERIFICAR_EXISTENCIA_USUARIO, email).get(0).get("quantidade", Integer.class);
        if (quantidade == 0) throw new UsuarioNaoExiste();
        Log.info("[UsuarioRepoImpl] Verificacao da existencia de um usuario com email " + email + " realizado com sucesso!");
    }

    @Override
    public String obterTokenAlteraoSenhaFinal(String email) {
        Log.info("[UsuarioRepoImpl] Iniciando busca do token de confirmacao final do usuario com email " + email + "...");
        String token = db.fetch(QUERY_OBTER_TOKEN_CONFIRMACAO_FINAL, email).get(0).get("token", String.class);
        Log.info("[UsuarioRepoImpl] Busca do token de confirmacao final do usuario com email " + email + " realizado com sucesso!");
        return token;
    }

    @Override
    public void verificarTokenFinal(String email, String token) {
        Log.info("[UsuarioRepoImpl] Iniciando a verificacao do token final para alteracao do usuario com email " + email + "...");
        Integer quantidade = db.fetch(QUERY_VERIFICAR_TOKEN_FINAL, token, email).get(0).get("quantidade", Integer.class);
        if (quantidade == 0) throw new TokenInvalido();
        Log.info("[UsuarioRepoImpl] Verificacao do token final para alteracao do usuario com email " + email + " realizado com sucesso!");
    }

    @Override
    public UsuarioDto atualizarToken(String email) {
        Log.info("[AutenticacaoUseCase] Iniciando atualizacao do token do usuario com email " + email + "...");
        Record usuarioAsRecord = db.fetch(QUERY_OBTER_USUARIO_TOKEN, email).get(0);
        UsuarioDto usuario = criarUsuarioDeResult(usuarioAsRecord);
        Log.info("[UsuarioRepoImpl] Iniciando criacao dos token de acesso para o usuario com email " + email);
        Pair<String, String> tokens = jwtService.gerarTokenAutenticacao(usuario.nome, email, usuarioAsRecord.get("id", Integer.class));
        usuario.adicionarTokens(tokens);
        Log.info("[AutenticacaoUseCase] Atualizacao do token do usuario com email " + email + " realizado com sucesso!");
        return usuario;
    }

    private UsuarioDto criarUsuarioDeResult(Record record) {
        return UsuarioDto.instanciar(
                record.get("nome", String.class),
                record.get("email", String.class)
        );
    }
}
