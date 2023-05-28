package br.com.academia.application.usecase.exception;

public class UsuarioNaoExiste extends RuntimeException{
    @Override
    public String toString() {
        return "Nao existe um usuario com essas informacoes!";
    }
}
