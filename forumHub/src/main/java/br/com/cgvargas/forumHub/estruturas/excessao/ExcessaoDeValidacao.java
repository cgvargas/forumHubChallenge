package br.com.cgvargas.forumHub.estruturas.excessao;

public class ExcessaoDeValidacao extends RuntimeException {
    public ExcessaoDeValidacao(String message) {
        super(message);
    }
}
