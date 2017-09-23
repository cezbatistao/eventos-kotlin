package br.com.cdcorp.eventos.infrastructure.notificacao

import br.com.cdcorp.eventos.service.NotificacaoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

/**
 * Created by ceb on 10/07/17.
 */
@Component
class NotificacaoEventProducer : NotificacaoService {

    private var publisher: ApplicationEventPublisher

    @Autowired
    constructor(publisher: ApplicationEventPublisher) {
        this.publisher = publisher
    }

    override fun enviarEmail(para: String, assunto: String, mensagem: String) {
        publisher.publishEvent(NotificacaoEmail(para, assunto, mensagem))
    }
}

class NotificacaoEmail(val para: String, val assunto: String, val mensagem: String) {

    override fun toString(): String {
        return "NotificacaoEmail(para='$para', assunto='$assunto', mensagem='$mensagem')"
    }
}
