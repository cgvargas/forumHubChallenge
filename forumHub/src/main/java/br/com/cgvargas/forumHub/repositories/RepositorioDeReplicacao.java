package br.com.cgvargas.forumHub.repositories;

import br.com.cgvargas.forumHub.dominios.replicadores.Replicador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RepositorioDeReplicacao extends JpaRepository<Replicador, Long> {
    @Query("SELECT r FROM Reply r WHERE r.topic.id = :topicId")
    List<Replicador> findAllFromTopic(@Param("topicId") Long id);
}
