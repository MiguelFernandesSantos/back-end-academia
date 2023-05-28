package br.com.academia.infrastructure.service;


import br.com.academia.application.service.TokenService;
import br.com.academia.dto.Pair;
import io.quarkus.logging.Log;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@ApplicationScoped
public class TokenJwtService implements TokenService {

    @Override
    public Pair<String, String> gerarTokenAutenticacao(String nome, String email, Integer usuarioId) {
        Log.info("[TokenJwtService] Iniciando geracao dos tokens para o usuario com email " + email + "...");
        Pair<String, String> tokens = new Pair<>(gerarTokenAutenticacao(nome, email, usuarioId, 3), gerarTokenAutenticacao(nome, email, usuarioId, 5));
        Log.info("[TokenJwtService] Geracao dos tokens para o usuario com email " + email + " realizado com sucesso!");
        return tokens;
    }

    @Override
    public String gerarTokenAlteracaoSenha() {
        throw new RuntimeException();
    }

    private String gerarTokenAutenticacao(String nome, String email, Integer usuarioId, Integer quantidadeDias) {
        return Jwt
                .issuer("chamados.com.br")
                .subject("autenticacao-chamados")
                .claim("email", email)
                .claim("nome", nome)
                .claim("user_id", usuarioId)
                .expiresIn(Duration.of(quantidadeDias, ChronoUnit.DAYS))
                .sign();
    }

}
