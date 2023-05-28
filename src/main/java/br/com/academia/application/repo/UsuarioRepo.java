package br.com.academia.application.repo;


import br.com.academia.dto.UsuarioDto;

public interface UsuarioRepo {

    void validarInformacoes(String email, String senha);

    UsuarioDto autenticarUsuario(String email, String senha);

    void verificarExistencia(String email);

    void criarNovoUsuario(String nome,  String email, String senha);

    void solicitarAlteracaoSenha(String email);

    void validarTokenAlteracaoSenha(String email, String token);

    void alterarSenha(String email, String senha, String token);

    void verificarSeUsuarioNaoExiste(String email);

    String obterTokenAlteraoSenhaFinal(String email);

    void verificarTokenFinal(String email, String token);

    UsuarioDto atualizarToken(String email);
}
