package br.com.academia.Interfaces;

import br.com.academia.application.usecase.AtividadeUseCase;
import br.com.academia.dto.NovaAtividade;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.Claim;

@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/api/academia/atividade")
public class AtividadeController {

    @Inject
    AtividadeUseCase useCase;

    @Inject
    @Claim("email")
    String email;

    @GET
    public Response obterAtividadePaginado(@QueryParam("pesquisa") String pesquisa, @QueryParam("data") String data, @QueryParam("ultimaAtividade") Integer ultimaAtividade, @QueryParam("limite") Integer limite) {
        return useCase.obterAtividadePaginado(pesquisa, data, ultimaAtividade, limite);
    }

    @GET
    @Path("/{numero}")
    public Response obterAtividadeEspecifica(@PathParam("numero") Integer numero) {
        return useCase.obterAtividadeEspecifica(numero);
    }

    @POST
    public Response criarNovaAtividade(NovaAtividade novaAtividade){
        return useCase.criarNovaAtividade();
    }

}
