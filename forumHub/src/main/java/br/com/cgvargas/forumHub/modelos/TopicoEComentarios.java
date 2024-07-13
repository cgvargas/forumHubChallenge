package br.com.cgvargas.forumHub.modelos;

import br.com.cgvargas.forumHub.dominios.cursos.Course;
import br.com.cgvargas.forumHub.dominios.topicos.Estado;
import br.com.cgvargas.forumHub.dominios.topicos.Topic;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record TopicoEComentarios(
        Long id,
        String title,
        LocalDateTime creation_date,
        String message,
        Course course,
        Estado estado,
        List<RespostaInfo> replies
) {
    public TopicoEComentarios(Topic topic) {
        this(topic.getId(),
                topic.getMessage(),
                topic.getCreationDate(),
                topic.getMessage(),
                topic.getCourse(),
                topic.getEstado(),
                topic.getReplies().stream()
                        .map(RespostaInfo::new)
                        .collect(Collectors.toList())
        );
    }
}

