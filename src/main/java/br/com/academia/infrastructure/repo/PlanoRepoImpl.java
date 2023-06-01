package br.com.academia.infrastructure.repo;

import br.com.academia.application.repo.PlanoRepo;
import br.com.academia.dto.Plano;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;

import java.util.List;

@ApplicationScoped
public class PlanoRepoImpl implements PlanoRepo {

    private static final String QUERY_BUSCAR_PLANOS = "";
    @Inject
    DSLContext db;

    @Override
    public List<Plano> obterPlanos() { // TODO terminar
        Log.info("");
        Result<Record> planosAsResult = db.fetch(QUERY_BUSCAR_PLANOS);
        Log.info("");
        List<Plano> planos = planosAsResult.map(this::instanciarPlanoDeRecord).stream().toList();
        Log.info("");
        return planos;
    }

    private Plano instanciarPlanoDeRecord(Record record) { // TODO terminar
        return new Plano();
    }

}
