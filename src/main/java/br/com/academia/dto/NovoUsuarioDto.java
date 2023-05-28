package br.com.academia.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class NovoUsuarioDto {

    public String nome;
    public String email;
    public String senha;

}
