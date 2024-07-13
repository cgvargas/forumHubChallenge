package br.com.cgvargas.forumHub.modelos;

import br.com.cgvargas.forumHub.dominios.replicadores.Replicador;

import java.time.LocalDateTime;

public record RespostaInfo(
        Long id,
        String message,
        LocalDateTime data,
        Boolean soluction,
        Long topic_id

) {
    public RespostaInfo(Replicador replicador) {
        this(replicador.getId(), replicador.getMessage(), replicador.getCreationDate(), replicador.getSoluction(), replicador.getTopic().getId());
    }
}


