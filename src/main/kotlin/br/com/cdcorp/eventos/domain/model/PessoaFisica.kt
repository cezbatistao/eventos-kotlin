package br.com.cdcorp.eventos.domain.model

import java.time.LocalDate


/**
 * Created by ceb on 02/07/17.
 */
class PessoaFisica(nome: String, email: String, celular: String, val dataNascimento: LocalDate, val cpf: String,
                   var tipoPessoaFisica: TipoPessoaFisica) : Pessoa(nome, email, celular) {

    var rg: String? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as PessoaFisica

        if (cpf != other.cpf) return false

        return true
    }

    override fun hashCode(): Int {
        return cpf.hashCode()
    }
}