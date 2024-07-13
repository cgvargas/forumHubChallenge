package br.com.cgvargas.forumHub.modelos;

import jakarta.validation.constraints.NotNull;

public record AtualizacaoResposta(
        @NotNull
        Long reply_id,
        String message,
        Boolean soluction
) {
}