package br.com.cdcorp.eventos.domain.model

/**
 * Created by ceb on 06/07/17.
 */
class Login(var login: String, var senha: String) {

    var id: Long? = null
    var ativo: Boolean = true
    var cadastroConfirmado: Boolean = false

}