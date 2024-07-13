package br.com.cgvargas.forumHub.servicos;

import br.com.cgvargas.forumHub.dominios.replicadores.Replicador;
import br.com.cgvargas.forumHub.dominios.topicos.Estado;
import br.com.cgvargas.forumHub.dominios.topicos.Topic;
import br.com.cgvargas.forumHub.modelos.RespostaCriacaoInfo;
import br.com.cgvargas.forumHub.modelos.RespostaInfo;
import br.com.cgvargas.forumHub.modelos.AtualizacaoResposta;
import br.com.cgvargas.forumHub.estruturas.excessao.ExcessaoDeValidacao;
import br.com.cgvargas.forumHub.estruturas.seguranca.ValidacaoDeSeguranca;
import br.com.cgvargas.forumHub.repositories.RepositorioDeReplicacao;
import br.com.cgvargas.forumHub.repositories.RepositorioDeTopicos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ServicoDeReplicacao {

    @Autowired
    private RepositorioDeReplicacao repositorioDeReplicacao;
    @Autowired
    private RepositorioDeTopicos repositorioDeTopicos;
    @Autowired
    private ValidacaoDeSeguranca validacaoDeSeguranca;

    public RespostaInfo create(Long topicId, RespostaCriacaoInfo data, String token) {
        var user = validacaoDeSeguranca.getUser(token);
        var topic = getTopicFromDB(topicId);
        var reply = new Replicador(null, data.message(), LocalDateTime.now(), false, topic, user);
        repositorioDeReplicacao.save(reply);
        return new RespostaInfo(reply);
    }

    public RespostaInfo update(AtualizacaoResposta data, Long topicId, String token) {
        var reply = repositorioDeReplicacao.getReferenceById(data.reply_id());
        if (isNotBlank(data.message())) {
            if (validacaoDeSeguranca.haveAuthorities(reply.getUser(), token)) {
                reply.setMessage(data.message());
            } else {
                throw new ExcessaoDeValidacao("Usuário não possui autorização para esta ação");
            }
        }
        if (data.soluction() != null) {
            if (validacaoDeSeguranca.haveAuthorities(reply.getTopic().getUser(), token)) {
                if (data.soluction()) {
                    setAllReplyToFalse(topicId);
                    reply.setSoluction(true);
                    reply.getTopic().setEstado(Estado.RESOLVIDO);
                } else {
                    reply.setSoluction(false);
                    reply.getTopic().setEstado(Estado.ATIVO);
                }
            } else {
                throw new ExcessaoDeValidacao("Usuário não possui autorização para esta ação");
            }
        }
        return new RespostaInfo(reply);
    }

    private boolean setAllReplyToFalse(Long topicId) {
        var topic = repositorioDeTopicos.getReferenceById(topicId);
        var replies = repositorioDeReplicacao.findAllFromTopic(topic.getId());
        try {
            for (Replicador replicador : replies) {
                replicador.setSoluction(false);
            }
            return true;
        } catch (Exception ex) {
            throw new ExcessaoDeValidacao(ex.getMessage());
        }
    }

    private Topic getTopicFromDB(Long topicId) {
        if (!repositorioDeTopicos.existsById(topicId)) {
            throw new ExcessaoDeValidacao("Id do tópico informado não existe");
        }
        return repositorioDeTopicos.getReferenceById(topicId);
    }

    public boolean delete(Long replyId, String token) {
        var user = repositorioDeReplicacao.findById(replyId).orElseThrow(() -> new ExcessaoDeValidacao("Id da resposta informado não existe")).getUser();
        if (validacaoDeSeguranca.haveAuthorities(user, token)) {
            repositorioDeReplicacao.deleteById(replyId);
            return true;
        }
        return false;
    }

    private boolean isNotBlank(String str) {
        return str != null && !str.trim().isEmpty();
    }
}
