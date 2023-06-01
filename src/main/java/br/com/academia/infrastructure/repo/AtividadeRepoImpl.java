package br.com.academia.infrastructure.repo;

import br.com.academia.application.repo.AtividadeRepo;
import br.com.academia.dto.Atividade;
import br.com.academia.dto.ResumoAtividade;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;

import java.util.List;

@ApplicationScoped
public class AtividadeRepoImpl implements AtividadeRepo {

    @Inject
    DSLContext db;

    private static final String QUERY_BUSCAR_ATIVIDADES = "";

    private static final String QUERY_OBTER_ATIVIDADE_ESPECIFICA = "";

    private static final String QUERY_CRIAR_NOVO_CHAMADO = "";

    @Override
    public List<ResumoAtividade> obterAtividadePaginado(String pesquisa, String data, Integer ultimaAtividade, Integer limite) {// TODO escrever
        Result<Record> atividadesAsResult = db.fetch(QUERY_BUSCAR_ATIVIDADES, pesquisa, data, ultimaAtividade, limite);
        List<ResumoAtividade> resumos = atividadesAsResult.map(this::instanciarDeRecord).stream().toList();
        return resumos;
    }

    @Override
    public Atividade obterAtividadeEspecifica(Integer id) {
        Record atividadeAsResult = db.fetch(QUERY_OBTER_ATIVIDADE_ESPECIFICA, id).get(0);
        Atividade atividade = instanciarAtividadeDeRecord(atividadeAsResult);
        return atividade;
    }

    @Override
    @Transactional
    public void criarNovaAtividade() {
        db.execute(QUERY_CRIAR_NOVO_CHAMADO);
    }

    private ResumoAtividade instanciarDeRecord(Record record) {
        return new ResumoAtividade(); // TODO escrever
    }

    private Atividade instanciarAtividadeDeRecord(Record record) {
        return new Atividade(); // TODO escrever
    }

}
