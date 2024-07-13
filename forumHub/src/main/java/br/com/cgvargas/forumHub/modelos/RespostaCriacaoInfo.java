package br.com.cgvargas.forumHub.modelos;

import jakarta.validation.constraints.NotBlank;

public record RespostaCriacaoInfo(
        @NotBlank
        String message
) {
}
