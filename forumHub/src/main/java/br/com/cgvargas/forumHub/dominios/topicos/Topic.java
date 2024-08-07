package br.com.cgvargas.forumHub.dominios.topicos;

import br.com.cgvargas.forumHub.dominios.cursos.Course;
import br.com.cgvargas.forumHub.dominios.replicadores.Replicador;
import br.com.cgvargas.forumHub.dominios.usuarios.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "topicos")
@Entity(name = "Topic")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String title;
    @Column(unique = true)
    private String message;
    private LocalDateTime creationDate;
    private Estado estado;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;
    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL)
    private List<Replicador> replies = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Topic(String title, String message, LocalDateTime now, Estado estado, Course course, User user) {
        this.title = title;
        this.message = message;
        this.creationDate = now;
        this.estado = estado;
        this.course = course;
        this.user = user;
    }

}