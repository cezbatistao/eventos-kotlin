package br.com.cdcorp.eventos.domain.model

/**
 * Created by ceb on 02/07/17.
 */
class Endereco(var logradouro: String, var numero: String, var complemento: String? = null, var cep: String,
               var bairro: String, var estado: String, var cidade: String) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as Endereco

        if (logradouro != other.logradouro) return false
        if (complemento != other.complemento) return false
        if (numero != other.numero) return false
        if (cep != other.cep) return false
        if (bairro != other.bairro) return false
        if (estado != other.estado) return false
        if (cidade != other.cidade) return false

        return true
    }

    override fun hashCode(): Int {
        var result = logradouro.hashCode()
        result = 31 * result + (complemento?.hashCode() ?: 0)
        result = 31 * result + numero.hashCode()
        result = 31 * result + cep.hashCode()
        result = 31 * result + bairro.hashCode()
        result = 31 * result + estado.hashCode()
        result = 31 * result + cidade.hashCode()
        return result
    }

    override fun toString(): String {
        return "Endereco(logradouro='$logradouro', numero='$numero, complemento=$complemento', cep='$cep', bairro='$bairro', " +
                "estado='$estado', cidade='$cidade')"
    }


}