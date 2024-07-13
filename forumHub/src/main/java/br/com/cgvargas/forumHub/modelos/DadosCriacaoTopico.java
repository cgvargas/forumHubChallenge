package br.com.cgvargas.forumHub.modelos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosCriacaoTopico(
        @NotBlank
        String title,
        @NotBlank
        String message,
        @NotNull
        Long course_id
){
}