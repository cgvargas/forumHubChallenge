package br.com.cgvargas.forumHub.modelos;

import br.com.cgvargas.forumHub.dominios.topicos.Estado;

public record AtualizacaoDoTopico(
        String title,
        String message,
        Estado estado,
        Long course_id
) {}