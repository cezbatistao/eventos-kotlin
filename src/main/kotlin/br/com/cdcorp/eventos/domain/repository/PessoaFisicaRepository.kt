package br.com.cdcorp.eventos.domain.repository

import br.com.cdcorp.eventos.domain.model.PessoaFisica

/**
 * Created by ceb on 02/07/17.
 */
interface PessoaFisicaRepository {

    fun salvar(pessoaFisica: PessoaFisica) : PessoaFisica
    fun findByCpf(cpf: String) : PessoaFisica?
    fun  get(id: Long) : PessoaFisica?
    fun atualizar(pessoaFisica: PessoaFisica): PessoaFisica

}