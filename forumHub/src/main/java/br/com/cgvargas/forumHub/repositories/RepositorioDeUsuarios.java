package br.com.cgvargas.forumHub.repositories;

import br.com.cgvargas.forumHub.dominios.usuarios.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RepositorioDeUsuarios extends JpaRepository<User, UUID> {
    UserDetails findByEmail(String email);
    Boolean existsByName(String name);
}
