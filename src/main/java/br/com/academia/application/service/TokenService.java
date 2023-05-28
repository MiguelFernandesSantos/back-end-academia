package br.com.academia.application.service;


import br.com.academia.dto.Pair;

public interface TokenService {

    Pair<String, String> gerarTokenAutenticacao(String nome, String email, Integer usuarioId);

    String gerarTokenAlteracaoSenha();

}
