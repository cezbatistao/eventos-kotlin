package br.com.cdcorp.eventos.service.impl

import br.com.cdcorp.eventos.domain.model.Login
import br.com.cdcorp.eventos.domain.repository.LoginRepository
import br.com.cdcorp.eventos.infrastructure.encode.CriptografiaSenha
import br.com.cdcorp.eventos.service.ConfiguracaoService
import br.com.cdcorp.eventos.service.NotificacaoService
import br.com.cdcorp.eventos.service.exception.NotificacaoErro
import br.com.cdcorp.eventos.service.exception.NotificacaoException
import br.com.cdcorp.eventos.support.UnitTest
import com.nhaarman.mockito_kotlin.eq
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Test
import org.mockito.Matchers.anyObject
import org.mockito.Matchers.anyString
import org.powermock.api.mockito.PowerMockito.*
import org.powermock.core.classloader.annotations.PrepareForTest

/**
 * Created by ceb on 08/07/17.
 */
@PrepareForTest(LoginServiceImpl::class)
class LoginServiceImplUnitTests : UnitTest() {

    private lateinit var loginRepository: LoginRepository

    private lateinit var loginService: LoginServiceImpl
    private lateinit var notificacaoService: NotificacaoService

    private lateinit var configuracaoService: ConfiguracaoService
    private lateinit var criptografiaSenha: CriptografiaSenha

    @Before
    fun setup() {
        configuracaoService = mock(ConfiguracaoService::class.java)
        criptografiaSenha = mock(CriptografiaSenha::class.java)

        loginRepository = mock(LoginRepository::class.java)

        notificacaoService = mock(NotificacaoService::class.java)
        loginService = spy(LoginServiceImpl(notificacaoService, loginRepository, configuracaoService, criptografiaSenha))
    }

    @Test
    fun `Validar login já criado para o email informado`() {
        val email = "carlos@carlos.com.br"

        `when`(loginRepository.findByEmail(email)).thenReturn(Login(email, "qualquer senha"))

        var notificacaoErro: NotificacaoErro? = null
        try {
            loginService.gerarLoginUsuario(email)
        } catch (ex: NotificacaoException) {
            val notificacoes = ex.notificacoes
            Assertions.assertThat(notificacoes).hasSize(1)
            notificacaoErro = notificacoes[0]
        }

        Assertions.assertThat(notificacaoErro).isNotNull()
        Assertions.assertThat(notificacaoErro?.mensagem).isEqualTo("E-mail ${email} já cadastrado.")
    }

    @Test
    fun `Criação de um login para o email informado`() {
        `when`(configuracaoService.get("notificacao.loginGerado.assunto")).thenReturn("assuntoto")
        `when`(configuracaoService.get("notificacao.loginGerado.corpo")).thenReturn("corpo")
        `when`(criptografiaSenha.criptografar(anyString())).thenReturn("senha criptografada")

        val email = "carlos@carlos.com.br"

        doReturn("Mensagem...").`when`(loginService).gerarMensagemDeCadastroConcluido(anyString(), eq(email), anyString())

        val login = loginService.gerarLoginUsuario(email)

        Assertions.assertThat(login).isNotNull()
        Assertions.assertThat(login.login).isEqualTo(email)
        Assertions.assertThat(login.senha).isNotBlank()
        Assertions.assertThat(login.ativo).isEqualTo(true)
        Assertions.assertThat(login.cadastroConfirmado).isEqualTo(false)
    }
}
