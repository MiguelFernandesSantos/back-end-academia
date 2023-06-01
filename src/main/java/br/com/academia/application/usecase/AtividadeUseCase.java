package br.com.academia.application.usecase;

import br.com.academia.application.repo.AtividadeRepo;
import br.com.academia.dto.Atividade;
import br.com.academia.dto.ResumoAtividade;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

import java.util.List;

@ApplicationScoped
public class AtividadeUseCase {

    @Inject
    AtividadeRepo repo;

    public Response obterAtividadePaginado(String pesquisa, String data, Integer ultimaAtividade, Integer limite) {
        Log.info("[AtividadeUseCase] Iniciando busca das atividades que estejam  a partir do id de numero " + ultimaAtividade + "...");
        try {
            List<ResumoAtividade> atividades = repo.obterAtividadePaginado(pesquisa, data, ultimaAtividade, limite);
            Log.info("[AtividadeUseCase] Busca das atividades a partir do id de numero " + ultimaAtividade + " realizado com sucesso!");
            return Response.ok(atividades).build();
        } catch (Exception e) {
            Log.error("[AtividadeUseCase] Ocorreu um erro na busca das atividades que estejam a partir do id de numero " + ultimaAtividade + "!!", e);
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    public Response obterAtividadeEspecifica(Integer numero) {
        Log.info("[AtividadeUseCase] Iniciando busca da atividade de numero " + numero + "...");
        try {
            Atividade atividade = repo.obterAtividadeEspecifica(numero);
            Log.info("[AtividadeUseCase] Busca da atividade de numero " + numero + " realizada com sucesso!");
            return Response.ok(atividade).build();
        } catch (Exception e) {
            Log.error("[AtividadeUseCase] Ocorreu um erro na busca atividade de numero " + numero + "!!", e);
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    public Response criarNovaAtividade() {
        Log.info("[AtividadeUseCase] Iniciando criacao de um novo chamado...");
        try {
            repo.criarNovaAtividade();
            Log.info("[AtividadeUseCase] Criacao de um novo chamado realizado com sucesso!");
            return Response.ok().build();
        } catch (Exception e) {
            Log.error("[AtividadeUseCase] Ocorreu um erro ao tentar criar um novo chamado!!", e);
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);

        }
    }
}
