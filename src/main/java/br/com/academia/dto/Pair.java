package br.com.academia.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class Pair<T, U> {
    private final T valorEsquerdo;
    private final U valorDireito;

    public Pair(T esquerda, U direita) {
        this.valorEsquerdo = esquerda;
        this.valorDireito = direita;
    }

    public T obterValorEsquerdo(){
        return valorEsquerdo;
    }

    public U obterValorDireito(){
        return valorDireito;
    }

}
