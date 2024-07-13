package br.com.cgvargas.forumHub.controladores;

import br.com.cgvargas.forumHub.modelos.DadosCriacaoTopico;
import br.com.cgvargas.forumHub.modelos.InformacoesDoTopico;
import br.com.cgvargas.forumHub.modelos.AtualizacaoDoTopico;
import br.com.cgvargas.forumHub.estruturas.seguranca.ValidacaoDeSeguranca;
import br.com.cgvargas.forumHub.repositories.RepositorioDeTopicos;
import br.com.cgvargas.forumHub.servicos.ServicoDeTopicos;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/topicos")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Tópicos")
public class ControladorDeTopico {

    @Autowired
    private ServicoDeTopicos servicoDeTopicos;

    @Autowired
    private RepositorioDeTopicos repositorioDeTopicos;

    @Autowired
    private ValidacaoDeSeguranca validacaoDeSeguranca;

    @PostMapping
    @Transactional
    @Operation(summary = "Criar tópico", description = "Novo tópico")
    public ResponseEntity<InformacoesDoTopico> createTopic(@RequestBody @Valid DadosCriacaoTopico data, HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        InformacoesDoTopico dto = servicoDeTopicos.create(data, token);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    @Operation(summary = "Listar", description = "Listar os tópicos")
    public ResponseEntity<Page<InformacoesDoTopico>> getAllTopics(@PageableDefault(size = 10, sort = {"creationDate", "course"}) Pageable pageable) {
        Page<InformacoesDoTopico> topics = repositorioDeTopicos.findAll(pageable).map(InformacoesDoTopico::new);
        return ResponseEntity.ok(topics);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar", description = "Buscar tópico por ID")
    public ResponseEntity<InformacoesDoTopico> getTopic(@PathVariable Long id) {
        return repositorioDeTopicos.findByIdWithCourse(id)
                .map(topic -> ResponseEntity.ok(new InformacoesDoTopico(topic)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Transactional
    @Operation(summary = "Atualizar", description = "Atualizar tópico")
    public ResponseEntity<InformacoesDoTopico> editTopic(@RequestBody @Valid AtualizacaoDoTopico data, @PathVariable Long id, HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        InformacoesDoTopico dto = servicoDeTopicos.update(id, token, data);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    @Transactional
    @Operation(summary = "Apagar", description = "Apagar tópico por ID")
    public ResponseEntity<Void> deleteTopic(@PathVariable Long id, HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        if (validacaoDeSeguranca.haveAuthoritiesForTopic(id, token)) {
            repositorioDeTopicos.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.badRequest().build();
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        return request.getHeader("Authorization").replace("Bearer ", "");
    }
}
