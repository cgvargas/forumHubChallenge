package br.com.cgvargas.forumHub.controladores;

import br.com.cgvargas.forumHub.dominios.usuarios.User;
import br.com.cgvargas.forumHub.modelos.CriacaoDeUsuario;
import br.com.cgvargas.forumHub.modelos.DadosDoUsuario;
import br.com.cgvargas.forumHub.modelos.AtualizacaoDoUsuario;
import br.com.cgvargas.forumHub.estruturas.excessao.ExcessaoDeValidacao;
import br.com.cgvargas.forumHub.estruturas.seguranca.ValidacaoDeSeguranca;
import br.com.cgvargas.forumHub.repositories.RepositorioDeUsuarios;
import br.com.cgvargas.forumHub.servicos.ServicoDeUsuarios;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Tag(name = "Usuários")
public class ControleDeUsuario {

    private final RepositorioDeUsuarios repository;
    private final ValidacaoDeSeguranca validacaoDeSeguranca;
    private final ServicoDeUsuarios servicoDeUsuarios;

    @Autowired
    public ControleDeUsuario(RepositorioDeUsuarios repository, ValidacaoDeSeguranca validacaoDeSeguranca, ServicoDeUsuarios servicoDeUsuarios) {
        this.repository = repository;
        this.validacaoDeSeguranca = validacaoDeSeguranca;
        this.servicoDeUsuarios = servicoDeUsuarios;
    }

    @PostMapping
    @Transactional
    @Operation(summary = "Cadastrar", description = "Cadastro de usuários (Parâmetro 'role' apenas para ADMIN)")
    public ResponseEntity<DadosDoUsuario> registrarUsuario(@RequestBody @Valid CriacaoDeUsuario data, HttpServletRequest request) {
        if (repository.findByEmail(data.email()) != null || repository.existsByName(data.name())) {
            throw new ExcessaoDeValidacao("Usuário cadastrado");
        }
        User user = new User(data);
        repository.save(user);
        return ResponseEntity.ok(new DadosDoUsuario(user));
    }

    @PutMapping("/{email}")
    @Transactional
    @Operation(summary = "Atualizar", description = "Atualização através de email")
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<DadosDoUsuario> updateUser(@RequestBody @Valid AtualizacaoDoUsuario data, HttpServletRequest request, @PathVariable String email) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        DadosDoUsuario dto = servicoDeUsuarios.update(data, token, email);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{email}")
    @Transactional
    @Operation(summary = "Apagar", description = "Apagar através de email")
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<Void> deleteUser(@PathVariable String email, HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        if (servicoDeUsuarios.delete(email, token)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
