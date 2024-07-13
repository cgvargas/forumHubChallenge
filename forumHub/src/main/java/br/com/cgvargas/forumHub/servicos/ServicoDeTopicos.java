package br.com.cgvargas.forumHub.servicos;

import br.com.cgvargas.forumHub.dominios.cursos.Course;
import br.com.cgvargas.forumHub.dominios.topicos.Estado;
import br.com.cgvargas.forumHub.dominios.topicos.Topic;
import br.com.cgvargas.forumHub.modelos.DadosCriacaoTopico;
import br.com.cgvargas.forumHub.modelos.InformacoesDoTopico;
import br.com.cgvargas.forumHub.modelos.AtualizacaoDoTopico;
import br.com.cgvargas.forumHub.estruturas.excessao.ExcessaoDeValidacao;
import br.com.cgvargas.forumHub.estruturas.seguranca.ValidacaoDeSeguranca;
import br.com.cgvargas.forumHub.estruturas.seguranca.TokenDeServico;
import br.com.cgvargas.forumHub.repositories.RepositorioDeCurso;
import br.com.cgvargas.forumHub.repositories.RepositorioDeTopicos;
import br.com.cgvargas.forumHub.repositories.RepositorioDeUsuarios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ServicoDeTopicos {

    @Autowired
    private RepositorioDeTopicos repositorioDeTopicos;

    @Autowired
    private RepositorioDeCurso repositorioDeCurso;

    @Autowired
    private TokenDeServico tokenDeServico;

    @Autowired
    private RepositorioDeUsuarios userRepository;

    @Autowired
    private ValidacaoDeSeguranca validacaoDeSeguranca;

    public InformacoesDoTopico create(DadosCriacaoTopico data, String token) {
        checkForSimilarTopics(data.title(), data.message());
        var course = getCourse(data.course_id());
        var user = this.validacaoDeSeguranca.getUser(token);

        var topic = new Topic(data.title(), data.message(), LocalDateTime.now(), Estado.ATIVO, course, user);
        this.repositorioDeTopicos.save(topic);
        return new InformacoesDoTopico(topic);
    }

    public InformacoesDoTopico update(Long id, String token, AtualizacaoDoTopico data) {
        var topic = this.repositorioDeTopicos.getReferenceById(id);
        var existTitle = this.repositorioDeTopicos.existsSimilarTitle(data.title());
        var existsMessage = this.repositorioDeTopicos.existsSimilarMessage(data.message());
        if (this.validacaoDeSeguranca.haveAuthorities(topic.getUser(), token)) {
            if (isItBlank(data.title()) && !existTitle) {
                topic.setTitle(data.title());
            }
            if (existTitle) {
                throw new ExcessaoDeValidacao("Titulo existente!");
            }
            if (isItBlank(data.message()) && !existsMessage) {
                topic.setMessage(data.message());
            }
            if (existsMessage) {
                throw new ExcessaoDeValidacao("Mensagem existente!");
            }
            if (isItBlank(String.valueOf(data.course_id()))) {
                topic.setCourse(this.repositorioDeCurso.getReferenceById(data.course_id()));
            }
            if (isItBlank(String.valueOf(data.estado()))) {
                topic.setEstado(data.estado());
            }
        }
        return new InformacoesDoTopico(topic);
    }

    public Course getCourse(Long id) {
        if (!this.repositorioDeCurso.existsById(id)) {
            throw new ExcessaoDeValidacao("Id do curso não existe");
        }
        return this.repositorioDeCurso.getReferenceById(id);
    }

    private boolean isItBlank(String str) {
        return str != null && !str.trim().isEmpty();
    }

    private void checkForSimilarTopics(String title, String message) {
        if (this.repositorioDeTopicos.existsSimilarTitle(title) || this.repositorioDeTopicos.existsSimilarMessage(message)) {
            throw new ExcessaoDeValidacao("O titulo/ mensagem existente!");
        }
    }
}
