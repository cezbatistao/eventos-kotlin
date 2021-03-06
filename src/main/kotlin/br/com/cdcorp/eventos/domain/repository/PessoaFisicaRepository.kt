package br.com.cdcorp.eventos.domain.repository

import br.com.cdcorp.eventos.domain.model.PessoaFisica

/**
 * Created by ceb on 02/07/17.
 */
interface PessoaFisicaRepository {

    fun create(pessoaFisica: PessoaFisica) : PessoaFisica
    fun findByCpf(cpf: String) : PessoaFisica?
    fun get(id: Long) : PessoaFisica?
    fun update(pessoaFisica: PessoaFisica): PessoaFisica
    fun list(): List<PessoaFisica>

}