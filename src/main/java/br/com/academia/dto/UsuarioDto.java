package br.com.academia.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class UsuarioDto {

    public String nome;
    public String email;
    public String token;
    public String refrescToken;

    public static UsuarioDto instanciar(String nome, String email) {
        UsuarioDto dto = new UsuarioDto();
        dto.nome = nome;
        dto.email = email;
        return dto;
    }

    public void adicionarTokens(Pair<String, String> tokens) {
        this.token = tokens.obterValorEsquerdo();
        this.refrescToken = tokens.obterValorDireito();
    }

}
