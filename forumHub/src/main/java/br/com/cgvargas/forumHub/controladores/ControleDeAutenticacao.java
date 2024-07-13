package br.com.cgvargas.forumHub.controladores;

import br.com.cgvargas.forumHub.dominios.usuarios.User;
import br.com.cgvargas.forumHub.modelos.DadosAutenticacao;
import br.com.cgvargas.forumHub.modelos.TokenResponse;
import br.com.cgvargas.forumHub.estruturas.seguranca.TokenDeServico;
import br.com.cgvargas.forumHub.repositories.RepositorioDeUsuarios;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
@Tag(name = "Login")
public class ControleDeAutenticacao {

    private final AuthenticationManager manager;
    private final TokenDeServico tokenDeServico;
    private final RepositorioDeUsuarios userRepository;

    @Autowired
    public ControleDeAutenticacao(AuthenticationManager manager, TokenDeServico tokenDeServico, RepositorioDeUsuarios userRepository) {
        this.manager = manager;
        this.tokenDeServico = tokenDeServico;
        this.userRepository = userRepository;
    }

    @PostMapping
    @Operation(summary = "Login", description = "Autenticar usuário")
    public ResponseEntity<TokenResponse> login(@RequestBody @Valid DadosAutenticacao data) {
        var authToken = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var auth = manager.authenticate(authToken);
        var tokenJWT = tokenDeServico.gerarToken((User) auth.getPrincipal());
        return ResponseEntity.ok(new TokenResponse(tokenJWT));
    }
}
