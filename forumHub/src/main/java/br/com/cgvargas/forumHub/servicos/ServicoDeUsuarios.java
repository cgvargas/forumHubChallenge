package br.com.cgvargas.forumHub.servicos;

import br.com.cgvargas.forumHub.dominios.usuarios.User;
import br.com.cgvargas.forumHub.dominios.usuarios.FuncaoDoUsuario;
import br.com.cgvargas.forumHub.modelos.DadosDoUsuario;
import br.com.cgvargas.forumHub.modelos.AtualizacaoDoUsuario;
import br.com.cgvargas.forumHub.estruturas.excessao.ExcessaoDeValidacao;
import br.com.cgvargas.forumHub.estruturas.seguranca.ValidacaoDeSeguranca;
import br.com.cgvargas.forumHub.repositories.RepositorioDeUsuarios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class ServicoDeUsuarios {

    @Autowired
    private ValidacaoDeSeguranca validacaoDeSeguranca;

    @Autowired
    private RepositorioDeUsuarios userRepository;

    public DadosDoUsuario update(AtualizacaoDoUsuario data, String token, String email) {
        var receivedUser = (User) this.userRepository.findByEmail(email);
        if (validacaoDeSeguranca.haveAuthorities(receivedUser, token)) {
            if (isItBlank(data.name())) {
                receivedUser.setName(data.name());
            }
            if (isItBlank(data.email()) && userRepository.findByEmail(data.email()) == null) {
                receivedUser.setEmail(data.email());
            }
            if (isItBlank(data.password())) {
                validarSenha(data.password());
                receivedUser.setPassword(data.password());
            }
        }
        if (validacaoDeSeguranca.isAdmin(token)) {
            if (isItBlank(data.role())) {
                try {
                    receivedUser.setRole(FuncaoDoUsuario.valueOf(data.role()));
                } catch (Exception ex) {
                    throw new ExcessaoDeValidacao(ex.getMessage());
                }
            }
        }
        return new DadosDoUsuario(receivedUser);
    }

    private boolean isItBlank(String str) {
        return str != null && !str.trim().isEmpty();
    }

    private void validarSenha(String senha) {
        if (senha == null || senha.length() < 8 ||
                !senha.matches(".*\\d.*") ||
                !senha.matches(".*[a-z].*") ||
                !senha.matches(".*[A-Z].*") ||
                !senha.matches(".*[$*&@#].*")) {
            throw new ExcessaoDeValidacao("A senha deve ter pelo menos 8 caracteres e incluir letras maiúsculas, minúsculas, números e caracteres especiais ($*&@#).");
        }
    }

    public Boolean delete(String email, String token) {
        var user = (User) userRepository.findByEmail(email);
        if (validacaoDeSeguranca.haveAuthorities(user, token)) {
            userRepository.deleteById(user.getId());
            return true;
        }
        return false;
    }
}
