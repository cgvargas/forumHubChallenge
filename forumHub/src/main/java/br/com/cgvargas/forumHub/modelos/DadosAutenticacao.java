package br.com.cgvargas.forumHub.modelos;

import jakarta.validation.constraints.NotBlank;

public record DadosAutenticacao(
        @NotBlank
        String email,
        @NotBlank
        String password) {
}
