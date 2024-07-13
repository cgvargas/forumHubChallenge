package br.com.cgvargas.forumHub.modelos;

public record AtualizacaoDoUsuario(
        String name,
        String email,
        String password,
        String role
) {
}