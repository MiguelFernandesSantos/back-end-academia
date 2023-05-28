package br.com.academia.application.usecase;


import br.com.academia.application.repo.UsuarioRepo;
import br.com.academia.application.usecase.exception.TokenInvalido;
import br.com.academia.application.usecase.exception.UsuarioExiste;
import br.com.academia.application.usecase.exception.UsuarioNaoExiste;
import br.com.academia.dto.UsuarioDto;
import br.com.academia.infrastructure.service.CriptografiaService;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class AutenticacaoUseCase {

    @Inject
    UsuarioRepo repo;

    public Response autenticarUsuario(String email, String senha) {
        Log.info("[AutenticacaoUseCase] Iniciando autenticacao do usuario com email " + email + "...");
        try {
            Log.info("[AutenticacaoUseCase] Criptografando senha");
            String senhaCriptografada = CriptografiaService.criptografarSenha(senha);
            Log.info("[AutenticacaoUseCase] Verificando a existencia de um usuario com email " + email);
            repo.validarInformacoes(email, senhaCriptografada);
            Log.info("[AutenticacaoUseCase] Autenticacando o usuario com email " + email);
            UsuarioDto usuario = repo.autenticarUsuario(email, senhaCriptografada);
            Log.info("[AutenticacaoUseCase] Autenticacao do usuario com email " + email + " realizado com sucesso!");
            return Response.ok(usuario).build();
        } catch (UsuarioNaoExiste e) {
            Log.error("[AutenticacaoUseCase] Nao existe um usuario com email " + email + "!!", e);
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } catch (Exception e) {
            Log.error("[AutenticacaoUseCase] Ocorreu um erro ao tentar autenticar o usuario com email " + email + "!!", e);
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    public Response criarNovoUsuario(String nome, String email, String senha) {
        Log.info("[AutenticacaoUseCase] Iniciando a criacao de um usuario com o nome " + nome + " e email " + email + "...");
        try {
            Log.info("[AutenticacaoUseCase] Criptografando senha");
            String senhaCriptografada = CriptografiaService.criptografarSenha(senha);
            Log.info("[AutenticacaoUseCase] Verificando a existencia de um usuario com o email " + email);
            repo.verificarExistencia(email);
            Log.info("[AutenticacaoUseCase] Criando usuario com o nome " + nome + " e email " + email);
            repo.criarNovoUsuario(nome, email, senhaCriptografada);
            Log.info("[AutenticacaoUseCase] Criacao do usuario com o nome " + nome + " e email " + email + " realizado com sucesso!");
            return Response.ok().build();
        } catch (UsuarioExiste e) {
            Log.info("[AutenticacaoUseCase] Nao foi possivel criar o  usuario com o nome " + nome + " e email " + email + " porque esse email ja esta sendo utilizado!!", e);
            throw new WebApplicationException(Response.Status.CONFLICT);
        } catch (Exception e) {
            Log.error("[AutenticacaoUseCase] Ocorreu um erro ao tentar criar um novo usuario para o email " + email + "!!", e);
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    public Response solicitarAlteracaoSenha(String email) {
        Log.info("[AutenticacaoUseCase] Iniciando solicitacao da alteracao da senha do email " + email + "...");
        try {
            Log.info("[AutenticacaoUseCase] Verificando a existencia de um usuario com o email " + email);
            repo.verificarSeUsuarioNaoExiste(email);
            Log.info("[AutenticacaoUseCase] Realizado ");
            repo.solicitarAlteracaoSenha(email);
            Log.info("[AutenticacaoUseCase] Solicitacao da alteracao da senha do email " + email + " realizado com sucesso");
            return Response.ok().build();
        } catch (UsuarioNaoExiste e) {
            Log.info("[AutenticacaoUseCase] Nao existe um usuario com o email " + email + "!!", e);
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } catch (Exception e) {
            Log.error("[AutenticacaoUseCase] Ocorreu um erro na solicitacao da alteracao da senha do email " + email + "!!", e);
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    public Response validarTokenAlteracaoSenha(String email, String token) {
        Log.info("[AutenticacaoUseCase] Iniciando validacao do token de alteracao de senha do email " + email + "...");
        try {
            repo.validarTokenAlteracaoSenha(email, token);
            Log.info("[AutenticacaoUseCase] Obtendo token de confirmacao da alteracao da senha");
            String tokenFinal = repo.obterTokenAlteraoSenhaFinal(email);
            Log.info("[AutenticacaoUseCase] Validacao e obtencao dos token realizado com sucesso!");
            return Response.ok(tokenFinal).build();
        } catch (TokenInvalido e) {
            Log.info("[AutenticacaoUseCase] O token passado nao esta correto!!", e);
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        } catch (Exception e) {
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    public Response alterarSenha(String email, String senha, String token) {
        Log.info("[AutenticacaoUseCase] Iniciando alteracao da senha do email " + email + "...");
        try {
            Log.info("[AutenticacaoUseCase] Verificando as informacoes de email e senha");
            repo.verificarTokenFinal(email, token);
            Log.info("[AutenticacaoUseCase] Criptografando senha");
            String senhaCriptografada = CriptografiaService.criptografarSenha(senha);
            Log.info("[AutenticacaoUseCase] Alterando senha do usuario com email " + email);
            repo.alterarSenha(email, senhaCriptografada, token);
            Log.info("[AutenticacaoUseCase] Alteracao da senha do email " + email + " realizado com sucesso!");
            return Response.ok().build();
        } catch (TokenInvalido e) {
            Log.info("[AutenticacaoUseCase] O token ou email passado nao sao validos!!", e);
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        } catch (Exception e) {
            Log.info("[AutenticacaoUseCase] Erro na alteracao da senha do email " + email + "!!", e);
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    public Response atualizarToken(String email) {
        Log.info("[AutenticacaoUseCase] Iniciando atualizacao do token do usuario com email " + email + "...");
        try {
            UsuarioDto usuario = repo.atualizarToken(email);
            Log.info("[AutenticacaoUseCase] Atualizacao do token do usuario com email " + email + " realizado com sucesso!");
            return Response.accepted(usuario).build();
        } catch (Exception e) {
            Log.info("[AutenticacaoUseCase] Erro na atualizacao do token referente ao usuario com email " + email + "!!", e);
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }
}
