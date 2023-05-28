package br.com.academia.application.usecase.exception;

public class TokenInvalido extends RuntimeException{
    @Override
    public String toString() {
        return "O token passado nao esta correto!";
    }
}
