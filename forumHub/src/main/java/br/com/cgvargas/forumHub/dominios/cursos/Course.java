package br.com.cgvargas.forumHub.dominios.cursos;

import br.com.cgvargas.forumHub.modelos.CursoDTO;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "Course")
@Table(name = "cursos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private Categorias categorias;

    public void update(CursoDTO data) {
        if(isNotBlank(data.name())){
            this.name = data.name();
        }
        if(data.categorias() != null){
            this.categorias = data.categorias();
        }
    }

    private boolean isNotBlank(String str) {
        return str != null && !str.trim().isEmpty();
    }
}
