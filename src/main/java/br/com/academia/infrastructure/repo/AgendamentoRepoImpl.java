package br.com.academia.infrastructure.repo;

import br.com.academia.application.repo.AgendamentoRepo;
import br.com.academia.dto.Agendamento;
import br.com.academia.dto.NovoAgendamento;
import br.com.academia.dto.ResumoAgendamento;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;

import java.util.List;

@ApplicationScoped
public class AgendamentoRepoImpl implements AgendamentoRepo {

    @Inject
    DSLContext db;
    private static final String QUERY_OBTER_AGENDAMENTOS_PAGINADO = "";
    private static final String QUERY_OBTER_AGENDAMENTO_ESPECIFICO = "";
    private static final String QUERY_CRIAR_NOVO_AGENDAMENTO = "";

    @Override
    public List<ResumoAgendamento> obterAgendamentosPaginado() { // TODO escrever metodo
        Log.info("");
        Result<Record> agendamentosAsResult = db.fetch(QUERY_OBTER_AGENDAMENTOS_PAGINADO);
        Log.info("");
        List<ResumoAgendamento> agendamentos = agendamentosAsResult.map(this::instanciarResumoAgendamentoDeRecord).stream().toList();
        Log.info("");
        return agendamentos;
    }

    @Override
    public Agendamento obterAgendamentoEspecifo(Integer id) { // TODO escrever metodo
        Log.info("");
        Record agendamentoAsRecord = db.fetch(QUERY_OBTER_AGENDAMENTO_ESPECIFICO, id).get(0);
        Log.info("");
        Agendamento agendamento = instanciarAgendamentoDeRecord(agendamentoAsRecord);
        Log.info("");
        return agendamento;
    }

    @Override
    @Transactional
    public void criarNovoAgendamento(NovoAgendamento novoAgendamento) { // TODO escrever metodo
        Log.info("");
        db.execute(QUERY_CRIAR_NOVO_AGENDAMENTO);
        Log.info("");
    }

    public ResumoAgendamento instanciarResumoAgendamentoDeRecord(Record record) { // TODO escrever metodo
        return new ResumoAgendamento();
    }

    public Agendamento instanciarAgendamentoDeRecord(Record record) { // TODO escrever metodo
        return new Agendamento();
    }

}
