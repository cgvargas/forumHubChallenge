package br.com.cgvargas.forumHub.modelos;

import br.com.cgvargas.forumHub.dominios.cursos.Categorias;
import br.com.cgvargas.forumHub.dominios.cursos.Course;

public record CursoDTO(
        Long id,
        String name,
        Categorias categorias) {

    public CursoDTO(Course course){
        this(course.getId(), course.getName(), course.getCategorias());
    }
}