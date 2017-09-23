package br.com.cdcorp.eventos.service

import br.com.cdcorp.eventos.domain.model.Login

/**
 * Created by ceb on 06/07/17.
 */
interface LoginService {

    fun criarLoginUsuario(email: String) : Login
    fun confirmarCadastro(email: String)
    fun desativarUsuario(email: String)
    fun trocarSenha(email: String, senha: String)

}