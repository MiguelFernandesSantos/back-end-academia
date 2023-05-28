package br.com.academia.Interfaces;

import br.com.academia.application.usecase.AutenticacaoUseCase;
import br.com.academia.dto.NovoUsuarioDto;
import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.Claim;

@ApplicationScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/api/academia/auth")
public class AutenticacaoController {

    @Inject
    AutenticacaoUseCase useCase;

    @Inject
    @Claim("email")
    String email;

    @POST
    @PermitAll
    @Path("/criar")
    public Response criarNovoUsuario(NovoUsuarioDto usuario) {
        return useCase.criarNovoUsuario(usuario.nome, usuario.email, usuario.senha);
    }

    @GET
    @PermitAll
    public Response autenticarUsuario(@QueryParam("email") String email, @QueryParam("senha") String senha) {
        return useCase.autenticarUsuario(email, senha);
    }

    @GET
    @PermitAll
    @Path("/refresc")
    public Response atualizarToken(){
        return  useCase.atualizarToken(email);
    }

    @PUT
    @PermitAll
    @Path("/senha/solicitar")
    public Response solicitarAlteracaoSenha(@QueryParam("email") String email) {
        return useCase.solicitarAlteracaoSenha(email);
    }

    @PUT
    @PermitAll
    @Path("/senha/validar")
    public Response validarTokenAlteracaoSenha(@QueryParam("email") String email, @QueryParam("token") String token) {
        return useCase.validarTokenAlteracaoSenha(email, token);
    }

    @PUT
    @Path("/senha/alterar")
    @PermitAll
    public Response alterarSenha(@QueryParam("email") String email, @QueryParam("senha") String senha, @QueryParam("token") String token) {
        return useCase.alterarSenha(email, senha, token);
    }

}