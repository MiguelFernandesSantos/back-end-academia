package br.com.academia.application.usecase.exception;

public class UsuarioExiste extends RuntimeException{
    @Override
    public String toString() {
        return  "Ja existe um usuario com esas informacoes!";
    }
}
