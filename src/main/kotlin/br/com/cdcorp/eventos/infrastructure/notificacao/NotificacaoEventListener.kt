package br.com.cdcorp.eventos.infrastructure.notificacao

import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

/**
 * Created by ceb on 10/07/17.
 */
@Component
class NotificacaoEventListener {

    @Async
    @EventListener
    fun handle(notificacaoEmail: NotificacaoEmail) {
        println("[${Thread.currentThread().getName()}] Notificação por email: ${notificacaoEmail}")
    }
}