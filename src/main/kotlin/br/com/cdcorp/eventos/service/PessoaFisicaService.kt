package br.com.cdcorp.eventos.service

import br.com.cdcorp.eventos.domain.model.PessoaFisica

/**
 * Created by ceb on 02/07/17.
 */
interface PessoaFisicaService {

    fun salvar(pessoaFisica: PessoaFisica) : PessoaFisica
    fun atualizar(pessoaFisica: PessoaFisica) : PessoaFisica
    fun findByCpf(cpf: String) : PessoaFisica?

}