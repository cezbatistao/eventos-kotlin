package br.com.cdcorp.eventos.domain.repository

import br.com.cdcorp.eventos.domain.model.Login

/**
 * Created by ceb on 10/07/17.
 */
interface LoginRepository {

    fun findByEmail(email: String) : Login?
    fun salvar(login: Login): Login
    fun get(id: Long): Login?

}