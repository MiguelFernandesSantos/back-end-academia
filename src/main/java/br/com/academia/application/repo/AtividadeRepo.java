package br.com.academia.application.repo;

import br.com.academia.dto.Atividade;
import br.com.academia.dto.ResumoAtividade;

import java.util.List;

public interface AtividadeRepo {

    List<ResumoAtividade> obterAtividadePaginado(String pesquisa, String data, Integer ultimaAtividade, Integer limite);

    Atividade obterAtividadeEspecifica(Integer id);

    void criarNovaAtividade();
}
