package br.com.cdcorp.eventos.service.exception

/**
 * Created by ceb on 06/07/17.
 */
class Notificacao {
    val notificacoes: MutableList<NotificacaoErro> = mutableListOf()

    fun addErro(erro: String, mensagem: String) {
        notificacoes.add(NotificacaoErro(erro, mensagem))
    }

    fun addErro(campo: String, erro: String, mensagem: String) {
        notificacoes.add(NotificacaoErro(campo, erro, mensagem))
    }

    fun temErro(): Boolean {
        return notificacoes.any()
    }

    fun throwErros() {
        if(temErro()) throw NotificacaoException(notificacoes)
    }
}