package br.com.cgvargas.forumHub.modelos;

import br.com.cgvargas.forumHub.dominios.topicos.Estado;
import br.com.cgvargas.forumHub.dominios.topicos.Topic;

import java.time.LocalDateTime;

public record InformacoesDoTopico(
        Long id,
        String title,
        LocalDateTime creation_date,
        String message,
        String course,
        Estado estado
) {
    public InformacoesDoTopico(Topic topic) {
        this(topic.getId(), topic.getTitle(),topic.getCreationDate() ,topic.getMessage(), topic.getCourse().getName(), topic.getEstado());
    }
}