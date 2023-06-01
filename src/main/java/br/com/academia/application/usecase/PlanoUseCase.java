package br.com.academia.application.usecase;

import br.com.academia.application.repo.PlanoRepo;
import br.com.academia.dto.Plano;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

import java.util.List;

@ApplicationScoped
public class PlanoUseCase {

    @Inject
    PlanoRepo repo;

    public Response obterPlanos() {
        Log.info("");
        try {
            List<Plano> planos = repo.obterPlanos();
            Log.info("");
            return Response.ok().build();
        } catch (Exception e) {
            Log.info("");
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }
}

/*
        Log.info("");
        try {

            Log.info("");
            return Response.ok().build();
        } catch (Exception e) {
            Log.info("");
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
 */