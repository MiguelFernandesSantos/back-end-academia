package br.com.academia.application.repo;

import br.com.academia.dto.Agendamento;
import br.com.academia.dto.NovoAgendamento;
import br.com.academia.dto.ResumoAgendamento;

import java.util.List;

public interface AgendamentoRepo {

    List<ResumoAgendamento> obterAgendamentosPaginado();

    Agendamento obterAgendamentoEspecifo(Integer id);

    void criarNovoAgendamento(NovoAgendamento novoAgendamento);
}
