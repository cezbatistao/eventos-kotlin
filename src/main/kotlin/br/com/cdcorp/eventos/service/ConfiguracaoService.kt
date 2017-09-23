package br.com.cdcorp.eventos.service

/**
 * Created by ceb on 12/07/17.
 */
interface ConfiguracaoService {

    fun get(propriedade: String): String

}