package br.com.cdcorp.eventos.service.exception

/**
 * Created by ceb on 06/07/17.
 */
class NotificacaoException(val notificacoes: MutableList<NotificacaoErro>) : RuntimeException() {

}
