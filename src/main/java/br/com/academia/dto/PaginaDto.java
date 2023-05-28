package br.com.academia.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.List;

@RegisterForReflection
public class PaginaDto<T> {
    public List<T> itens;
    public Integer total;
    public PaginaDto(List<T> itens, Integer total) {
        this.itens = itens;
        this.total = total;
    }

}
