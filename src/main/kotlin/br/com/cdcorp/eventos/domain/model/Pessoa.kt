package br.com.cdcorp.eventos.domain.model


/**
 * Created by ceb on 02/07/17.
 */
abstract class Pessoa(var nome: String, var email: String, var celular: String) {

//    init {
//        this.celular = celular.replace("[^\\x00-\\x7F]", "")
//        println(this.celular)
//    }

    var id: Long? = null
    var telefone: String? = null
    var endereco: Endereco? = null
    var login: Login? = null

}
