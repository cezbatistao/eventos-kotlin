package br.com.cdcorp.eventos.service.exception

/**
 * Created by ceb on 06/07/17.
 */
class NotificacaoErro(val campo: String?, val erro: String, val mensagem: String) {

    constructor(erro: String, mensagem: String) : this(null, erro, mensagem)

    override fun toString(): String {
        return "NotificacaoErro(campo=$campo, erro='$erro', mensagem='$mensagem')"
    }
}