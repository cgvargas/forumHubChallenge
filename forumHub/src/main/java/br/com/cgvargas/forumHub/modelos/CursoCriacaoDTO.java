package br.com.cgvargas.forumHub.modelos;

import br.com.cgvargas.forumHub.dominios.cursos.Categorias;
import br.com.cgvargas.forumHub.dominios.cursos.Course;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CursoCriacaoDTO(
        @NotBlank
        String name,
        @NotNull
        Categorias categorias) {

    public CursoCriacaoDTO(Course course){
        this(course.getName(), course.getCategorias());
    }

}
