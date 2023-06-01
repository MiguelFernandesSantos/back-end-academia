package br.com.academia.Interfaces;

import br.com.academia.application.usecase.AgendamentoUseCase;
import br.com.academia.dto.NovoAgendamento;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.Claim;

@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/api/academia/agendamento")
public class AgendamentoController {

    @Inject
    AgendamentoUseCase useCase;

    @Inject
    @Claim("email")
    String email;

    @GET
    public Response obterAgendamentosPaginado() {
        return useCase.obterAgendamentosPaginado();
    }

    @GET
    @Path("/{numero}")
    public Response obterAgendamentoEspecifico(@PathParam("numero") Integer numeroAgendamento) {
        return useCase.obterAgendamentoEspecifico(numeroAgendamento);
    }

    @POST
    public Response criarNovoAgendamento(NovoAgendamento novoAgendamento){
        return useCase.criarNovoAgendamento(novoAgendamento);
    }

}
