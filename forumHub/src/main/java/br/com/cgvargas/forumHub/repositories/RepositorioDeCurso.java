package br.com.cgvargas.forumHub.repositories;

import br.com.cgvargas.forumHub.dominios.cursos.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositorioDeCurso extends JpaRepository<Course, Long> {
    boolean existsByName(String name);
}
