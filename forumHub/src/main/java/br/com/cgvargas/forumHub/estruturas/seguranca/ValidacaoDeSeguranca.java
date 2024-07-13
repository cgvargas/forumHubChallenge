package br.com.cgvargas.forumHub.estruturas.seguranca;

import br.com.cgvargas.forumHub.dominios.usuarios.User;
import br.com.cgvargas.forumHub.repositories.RepositorioDeTopicos;
import br.com.cgvargas.forumHub.repositories.RepositorioDeUsuarios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidacaoDeSeguranca {

    @Autowired
    private RepositorioDeTopicos repositorioDeTopicos;
    @Autowired
    private TokenDeServico tokenDeServico;
    @Autowired
    private RepositorioDeUsuarios userRepository;


    public Boolean haveAuthoritiesForTopic(Long id, String token) {
        var topicUser = repositorioDeTopicos.getReferenceById(id).getUser();
        if(isTheSameUser(topicUser,getUser(token)) || isAdmin(token)){
            return true;
        }
        return false;
    }

    public Boolean haveAuthorities(User user, String token) {
        if(isTheSameUser(user,getUser(token)) || isAdmin(token)){
            return true;
        }
        return false;
    }

    public Boolean isTheSameUser(User topicUser, User receivedUser){
        if(topicUser == receivedUser) {return true;}
        return false;
    }

    public Boolean isAdmin(String token){
        var receivedUser = getUser(token);
        var test = receivedUser.getAuthorities().stream().anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
        return test;
    }

    public User getUser(String token) {
        var email = tokenDeServico.getSubject(token);
        var userDetails = this.userRepository.findByEmail(email);
        return (User) userDetails;
    }
}