package br.com.cdcorp.eventos.service.impl

import br.com.cdcorp.eventos.service.LoginService
import br.com.cdcorp.eventos.service.exception.NotificacaoErro
import br.com.cdcorp.eventos.service.exception.NotificacaoException
import br.com.cdcorp.eventos.support.IntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

/**
 * Created by ceb on 12/07/17.
 */
open class LoginServiceImplIntegrationTests : IntegrationTest() {

    @Autowired
    private lateinit var loginService: LoginService

    @Test
    fun `Criar um Login com dados válidos`() {
        val email = "carlos@carlos.com.br"

        val loginCriado = loginService.criarLoginUsuario(email)

        assertThat(loginCriado).isNotNull()
        assertThat(loginCriado.id).isNotNull()
        assertThat(loginCriado.login).isEqualTo(email)
        assertThat(loginCriado.senha).isNotEmpty()
        assertThat(loginCriado.ativo).isTrue()
        assertThat(loginCriado.cadastroConfirmado).isFalse()
    }

    @Test
    fun `Validar login já cadastrado`() {
        val email = "carlos@carlos.com.br"
        loginService.criarLoginUsuario(email)

        var notificacaoErro: NotificacaoErro? = null
        try {
            loginService.criarLoginUsuario(email)
        } catch (ex: NotificacaoException) {
            val notificacoes = ex.notificacoes
            assertThat(notificacoes).hasSize(1)
            notificacaoErro = notificacoes[0]
        }

        assertThat(notificacaoErro).isNotNull()
        assertThat(notificacaoErro?.erro).isEqualTo("registrao_ja_cadastrado")
        assertThat(notificacaoErro?.mensagem).isEqualTo("E-mail ${email} já cadastrado.")
    }
}
