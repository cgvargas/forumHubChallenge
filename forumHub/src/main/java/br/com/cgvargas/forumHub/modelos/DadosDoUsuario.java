package br.com.cgvargas.forumHub.modelos;

import br.com.cgvargas.forumHub.dominios.usuarios.User;
import br.com.cgvargas.forumHub.dominios.usuarios.FuncaoDoUsuario;

public record DadosDoUsuario(
        String nome,
        String email,
        FuncaoDoUsuario role
) {
    public DadosDoUsuario(User user) {
        this(user.getName(), user.getEmail(), user.getRole());
    }
}