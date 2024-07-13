package br.com.cgvargas.forumHub.controladores;

import br.com.cgvargas.forumHub.dominios.cursos.Course;
import br.com.cgvargas.forumHub.modelos.CursoCriacaoDTO;
import br.com.cgvargas.forumHub.modelos.CursoDTO;
import br.com.cgvargas.forumHub.estruturas.excessao.ExcessaoDeValidacao;
import br.com.cgvargas.forumHub.repositories.RepositorioDeCurso;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/cursos")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Cursos - Necessário permissão de ADMIN para acessar métodos que não sejam 'GET'")
public class ControleDeCurso {

    private final RepositorioDeCurso repository;

    @Autowired
    public ControleDeCurso(RepositorioDeCurso repository) {
        this.repository = repository;
    }

    @PostMapping
    @Transactional
    @Operation(summary = "Cadastrar", description = "Cadastrar um novo curso")
    public ResponseEntity<CursoDTO> registerCourse(@RequestBody @Valid CursoCriacaoDTO data) {
        if (repository.existsByName(data.name())) {
            throw new ExcessaoDeValidacao("Já existe um curso com esse nome cadastrado.");
        }
        var course = new Course(null, data.name(), data.categorias());
        repository.save(course);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(course.getId())
                .toUri();
        return ResponseEntity.created(location).body(new CursoDTO(course));
    }

    @GetMapping
    @Operation(summary = "Listar todos", description = "Lista todos os cursos cadastrados")
    public ResponseEntity<Page<CursoDTO>> getAllCourses(@PageableDefault(size = 10, sort = {"category", "name"}) Pageable pageable) {
        var courses = repository.findAll(pageable).map(CursoDTO::new);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar por ID", description = "Busca um curso pelo ID")
    public ResponseEntity<CursoDTO> getCourse(@PathVariable Long id) {
        var course = repository.findById(id);
        return course.map(value -> ResponseEntity.ok(new CursoDTO(value)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Transactional
    @Operation(summary = "Editar", description = "Editar um curso existente")
    public ResponseEntity<CursoDTO> editCourse(@RequestBody @Valid CursoDTO data, @PathVariable Long id) {
        var existingCourse = repository.findById(id);
        if (existingCourse.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var course = existingCourse.get();
        course.update(data);
        return ResponseEntity.ok(new CursoDTO(course));
    }

    @DeleteMapping("/{id}")
    @Transactional
    @Operation(summary = "Deletar", description = "Deletar um curso pelo ID")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
