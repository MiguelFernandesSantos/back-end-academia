package br.com.academia.Interfaces;

import br.com.academia.application.usecase.PlanoUseCase;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/api/academia/plano")
public class PlanoController {

    @Inject
    PlanoUseCase useCase;

    @GET
    public Response obterPlanos(){
        return useCase.obterPlanos();
    }

}
