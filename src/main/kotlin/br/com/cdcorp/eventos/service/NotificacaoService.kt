package br.com.cdcorp.eventos.service

import org.springframework.context.event.EventListener

/**
 * Created by ceb on 08/07/17.
 */
interface NotificacaoService {

    fun enviarEmail(para: String, assunto: String, mensagem: String)

}