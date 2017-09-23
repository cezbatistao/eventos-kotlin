package br.com.cdcorp.eventos.infrastructure.encode

import org.apache.commons.lang3.RandomStringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

/**
 * Created by ceb on 12/07/17.
 */
@Service
class CriptografiaSenha {

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    fun gerarSenha() : String {
        return RandomStringUtils.random(6, 0, 20, true, true, *"qw32rfHIJk9iQ8Ud7h0X".toCharArray())
    }

    fun criptografar(senha: String) : String {

        return passwordEncoder.encode(senha)
    }

    fun gerarSenhaCriptografada() : String {
        return criptografar(gerarSenha())
    }
}