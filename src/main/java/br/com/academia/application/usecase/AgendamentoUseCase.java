package br.com.academia.application.usecase;

import br.com.academia.application.repo.AgendamentoRepo;
import br.com.academia.dto.Agendamento;
import br.com.academia.dto.NovoAgendamento;
import br.com.academia.dto.ResumoAgendamento;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

import java.util.List;

@ApplicationScoped
public class AgendamentoUseCase {

    @Inject
    AgendamentoRepo repo;

    public Response obterAgendamentosPaginado() {
        Log.info("[AgendamentoUseCase] Iniciando busca dos agendamentos...");
        try {
            List<ResumoAgendamento> agendamentos = repo.obterAgendamentosPaginado();
            Log.info("[AgendamentoUseCase] Busca dos agendamentos realizado com sucesso!");
            return Response.ok(agendamentos).build();
        } catch (Exception e) {
            Log.error("[AgendamentoUseCase] Ocorreu um erro ao buscar os agendamentos!!", e);
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    public Response obterAgendamentoEspecifico(Integer numeroAgendamento) {
        Log.info("[AgendamentoUseCase] Iniciando busca do agendamento de numero " + numeroAgendamento + "...");
        try {
            Agendamento agendamento = repo.obterAgendamentoEspecifo(numeroAgendamento);
            Log.info("[AgendamentoUseCase] Busca do agendamento de numero " + numeroAgendamento + " realizado com sucesso!");
            return Response.ok(agendamento).build();
        } catch (Exception e) {
            Log.error("[AgendamentoUseCase] Ocorreu um erro ao tentar buscar o agendamento de numero " + numeroAgendamento + "!!", e);
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    public Response criarNovoAgendamento(NovoAgendamento novoAgendamento) {
        Log.info("[AgendamentoUseCase] Iniciando criacao de um novo agendamento");
        try {
            repo.criarNovoAgendamento(novoAgendamento);
            Log.info("[AgendamentoUseCase] Criacao de um novo agendamento realizado com sucesso!");
            return Response.ok().build();
        } catch (Exception e) {
            Log.info("[AgendamentoUseCase] Ocorreu um erro ao criar um novo agendamento!!", e);
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }
}
