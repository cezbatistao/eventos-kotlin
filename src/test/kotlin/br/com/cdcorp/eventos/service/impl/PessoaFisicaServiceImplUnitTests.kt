package br.com.cdcorp.eventos.service.impl

import br.com.cdcorp.eventos.domain.model.Endereco
import br.com.cdcorp.eventos.domain.model.Login
import br.com.cdcorp.eventos.domain.model.PessoaFisica
import br.com.cdcorp.eventos.domain.model.TipoPessoaFisica
import br.com.cdcorp.eventos.domain.repository.PessoaFisicaRepository
import br.com.cdcorp.eventos.service.NotificacaoService
import br.com.cdcorp.eventos.service.exception.NotificacaoErro
import br.com.cdcorp.eventos.service.exception.NotificacaoException
import br.com.cdcorp.eventos.support.UnitTest
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Matchers.anyString
import org.powermock.api.mockito.PowerMockito.`when`
import org.powermock.api.mockito.PowerMockito.mock
import org.powermock.core.classloader.annotations.PrepareForTest
import org.springframework.core.env.Environment
import org.springframework.security.crypto.password.PasswordEncoder
import org.tools4j.spockito.Spockito
import java.time.LocalDate


/**
 * Created by ceb on 02/07/17.
 */
@PrepareForTest(LoginServiceImpl::class)
class PessoaFisicaServiceImplUnitTests : UnitTest() {

    private lateinit var pessoaFisicaService: PessoaFisicaServiceImpl

    private lateinit var pessoaFisicaRepository: PessoaFisicaRepository
    private lateinit var loginService: LoginServiceImpl
    private lateinit var notificacaoService: NotificacaoService

    private lateinit var passwordEncoder: PasswordEncoder
    private lateinit var env: Environment

    @Before
    fun setup() {
        passwordEncoder = mock(PasswordEncoder::class.java)
        env = mock(Environment::class.java)

        pessoaFisicaRepository = mock(PessoaFisicaRepository::class.java)
        notificacaoService = mock(NotificacaoService::class.java)
        loginService = mock(LoginServiceImpl::class.java)

        pessoaFisicaService = PessoaFisicaServiceImpl(loginService, notificacaoService, pessoaFisicaRepository)
    }

    @Test
    @Spockito.Name(" com mensagem experada: {7}")
    @Spockito.Unroll(
        "| Nome     | Email               | Data Nascimento  | Celular          | CPF             | RG            | Tipo Pessoa Fisica  | Mensagem Experada       | ",
        "|          | carlos@carlos.com.br  | 1982-10-10       | (19) 99999-7777  | 350.518.412-87  | 33.333.333.3  | ESTUDANTE           | Nome é obrigatório.     | ",
        "| Carlos   |                     | 1982-10-10       | (19) 99999-7777  | 350.518.412-87  | 33.333.333.3  | ESTUDANTE           | E-mail é obrigatório.   | ",
        "| Carlos   | lfdshalefsrfse323   | 1982-10-10       | (19) 99999-7777  | 350.518.412-87  | 33.333.333.3  | ESTUDANTE           | E-mail inválido.        | ",
        "| Carlos   | carlos@carlos.com.br  | 1982-10-10       |                  | 350.518.412-87  | 33.333.333.3  | ESTUDANTE           | Celular é obrigatório.  | ",
        "| Carlos   | carlos@carlos.com.br  | 1982-10-10       | (19) 343243243   | 350.518.412-87  | 33.333.333.3  | ESTUDANTE           | Celular inválido.       | ",
        "| Carlos   | carlos@carlos.com.br  | 2015-10-10       | (19) 99999-7777  | 350.518.412-87  | 33.333.333.3  | ESTUDANTE           | Idade mínima para participar é de 18 anos.       | ",
        "| Carlos   | carlos@carlos.com.br  | 1982-10-10       | (19) 99999-7777  |                 | 33.333.333.3  | ESTUDANTE           | CPF é obrigatório.      | ",
        "| Carlos   | carlos@carlos.com.br  | 1982-10-10       | (19) 99999-7777  | 123.789.987-78  | 33.333.333.3  | ESTUDANTE           | CPF inválido.           | "
    )
    fun `Validando campos para criar Pessoa Física`(nome: String, email: String, dataNascimento: LocalDate,
                                                                 celular: String, cpf: String, rg: String, tipoPessoaFisica: TipoPessoaFisica,
                                                                 mensagemExperada: String) {
        val endereco = Endereco("Rua Sem Nome", "123", null, "12.345-678", "Nome do Bairro", "SP", "Campinas")
        val pessoaFisica = PessoaFisica(nome, email, celular, dataNascimento, cpf, tipoPessoaFisica)
        pessoaFisica.endereco = endereco

        var notificacaoErro: NotificacaoErro? = null
        try {
            pessoaFisicaService.salvar(pessoaFisica)
        } catch (ex: NotificacaoException) {
            val notificacoes = ex.notificacoes
            assertThat(notificacoes).hasSize(1)
            notificacaoErro = notificacoes[0]
        }

        assertThat(notificacaoErro).isNotNull()
        assertThat(notificacaoErro?.mensagem).isEqualTo(mensagemExperada)
    }

    @Test
    fun `Erro de Pessoa Fisica já cadastrada`() {
        val nome = "Carlos"
        val email = "carlos@carlos.com.br"
        val dataNascimento = LocalDate.of(1982, 10, 10)
        val celular = "(19) 99999-7777"
        val cpf = "350.518.412-87"
        val tipoPessoaFisica = TipoPessoaFisica.ESTUDANTE

        val pessoaFisica = PessoaFisica(nome, email, celular, dataNascimento, cpf, tipoPessoaFisica)

        val endereco = Endereco("Rua Sem Nome", "123", null, "12.345-678", "Nome do Bairro", "SP", "Campinas")
        pessoaFisica.endereco = endereco

        val pessoaFisicaCadastrada = PessoaFisica(nome, email, celular, dataNascimento, cpf, tipoPessoaFisica)
        pessoaFisicaCadastrada.id = 3213L
        pessoaFisicaCadastrada.endereco = endereco

        `when`(pessoaFisicaRepository.findByCpf(cpf)).thenReturn(pessoaFisicaCadastrada)

        var notificacaoErro: NotificacaoErro? = null
        try {
            pessoaFisicaService.salvar(pessoaFisica)
        } catch (ex: NotificacaoException) {
            val notificacoes = ex.notificacoes
            assertThat(notificacoes).hasSize(1)
            notificacaoErro = notificacoes[0]
        }

        assertThat(notificacaoErro).isNotNull()
        assertThat(notificacaoErro?.mensagem).isEqualTo("Pessoa física com CPF ${cpf} já cadastrado.")
    }

    @Test
    fun `Criar uma Pessoa Fisica com dados validos`() {
        val nome = "Carlos"
        val email = "carlos@carlos.com.br"
        val dataNascimento = LocalDate.of(1982, 10, 10)
        val celular = "(19) 99999-7777"
        val cpf = "350.518.412-87"
        val tipoPessoaFisica = TipoPessoaFisica.ESTUDANTE

        val endereco = Endereco("Rua Sem Nome", "123", null, "12.345-678", "Nome do Bairro", "SP", "Campinas")
        val pessoaFisica = PessoaFisica(nome, email, celular, dataNascimento, cpf, tipoPessoaFisica)
        pessoaFisica.endereco = endereco

        `when`(loginService.criarLoginUsuario(email)).thenReturn(Login(email, anyString()))

        pessoaFisicaService.salvar(pessoaFisica)

        val captor = argumentCaptor<PessoaFisica>()
        verify(pessoaFisicaRepository).salvar(captor.capture())

        val pessoaFisicaActual = captor.firstValue
        assertThat(pessoaFisicaActual).isNotNull()
        assertThat(pessoaFisicaActual.endereco).isNotNull()
        assertThat(pessoaFisicaActual.login).isNotNull()
    }

    @Test
    @Spockito.Name(" com mensagem experada: {6}")
    @Spockito.Unroll(
            "| Logradouro    | Numero  | CEP         | Bairro          | Estado  | Cidade    | Mensagem Experada          | ",
            "|               | 45712M  | 12.345-678  | Nome do Bairro  | SP      | Campinas  | Logradouro é obrigatório.  | ",
            "| Rua Sem Nome  |         | 12.345-678  | Nome do Bairro  | SP      | Campinas  | Número é obrigatório.      | ",
            "| Rua Sem Nome  | 45712M  |             | Nome do Bairro  | SP      | Campinas  | CEP é obrigatório.         | ",
            "| Rua Sem Nome  | 45712M  | 12.345-678  |                 | SP      | Campinas  | Bairro é obrigatório.      | ",
            "| Rua Sem Nome  | 45712M  | 12.345-678  | Nome do Bairro  |         | Campinas  | Estado é obrigatório.      | ",
            "| Rua Sem Nome  | 45712M  | 12.345-678  | Nome do Bairro  | SP      |           | Cidade é obrigatório.      | "
    )
    fun `Validando os campos de Endereço`(logradouro: String, numero: String, cep: String, bairro: String, estado: String,
                                          cidade: String, mensagemExperada: String) {
        val nome = "Carlos"
        val email = "carlos@carlos.com.br"
        val dataNascimento = LocalDate.of(1982, 10, 10)
        val celular = "(19) 99999-7777"
        val cpf = "350.518.412-87"
        val tipoPessoaFisica = TipoPessoaFisica.ESTUDANTE
        val pessoaFisica = PessoaFisica(nome, email, celular, dataNascimento, cpf, tipoPessoaFisica)

        val endereco = Endereco(logradouro, numero, null, cep, bairro, estado, cidade)
        pessoaFisica.endereco = endereco

        var notificacaoErro: NotificacaoErro? = null
        try {
            pessoaFisicaService.salvar(pessoaFisica)
        } catch (ex: NotificacaoException) {
            val notificacoes = ex.notificacoes
            assertThat(notificacoes).hasSize(1)
            notificacaoErro = notificacoes[0]
        }

        assertThat(notificacaoErro).isNotNull()
        assertThat(notificacaoErro?.mensagem).isEqualTo(mensagemExperada)
    }

    @Test
    fun `Erro ao atualizar Pessoa Fisica sem informar ID`() {
        val nome = "Carlos"
        val email = "carlos@carlos.com.br"
        val dataNascimento = LocalDate.of(1982, 10, 10)
        val celular = "(19) 99999-7777"
        val cpf = "350.518.412-87"
        val tipoPessoaFisica = TipoPessoaFisica.ESTUDANTE
        val pessoaFisica = PessoaFisica(nome, email, celular, dataNascimento, cpf, tipoPessoaFisica)

        val endereco = Endereco("Rua Sem Nome", "123", null, "12.345-678", "Nome do Bairro", "SP", "Campinas")
        pessoaFisica.endereco = endereco

        var notificacaoErro: NotificacaoErro? = null
        try {
            pessoaFisicaService.atualizar(pessoaFisica)
        } catch (ex: NotificacaoException) {
            val notificacoes = ex.notificacoes
            assertThat(notificacoes).hasSize(1)
            notificacaoErro = notificacoes[0]
        }

        assertThat(notificacaoErro).isNotNull()
        assertThat(notificacaoErro?.mensagem).isEqualTo("ID é obrigatório para atualizar.")
    }

    @Test
    fun `Erro ao atualizar Pessoa Fisica ID sem cadastro`() {
        val id = 23432L
        val nome = "Carlos"
        val email = "carlos@carlos.com.br"
        val dataNascimento = LocalDate.of(1982, 10, 10)
        val celular = "(19) 99999-7777"
        val cpf = "350.518.412-87"
        val tipoPessoaFisica = TipoPessoaFisica.ESTUDANTE
        val pessoaFisica = PessoaFisica(nome, email, celular, dataNascimento, cpf, tipoPessoaFisica)
        pessoaFisica.id = id

        val endereco = Endereco("Rua Sem Nome", "123", null, "12.345-678", "Nome do Bairro", "SP", "Campinas")
        pessoaFisica.endereco = endereco

        `when`(pessoaFisicaRepository.get(id)).thenReturn(null)

        var notificacaoErro: NotificacaoErro? = null
        try {
            pessoaFisicaService.atualizar(pessoaFisica)
        } catch (ex: NotificacaoException) {
            val notificacoes = ex.notificacoes
            assertThat(notificacoes).hasSize(1)
            notificacaoErro = notificacoes[0]
        }

        assertThat(notificacaoErro).isNotNull()
        assertThat(notificacaoErro?.mensagem).isEqualTo("Não existe cadastro de Pessoa Fisica com o ID ${id}.")
    }

    @Test
    fun `Atualizar dados de Pessoa Fisica`() {
        val id = 23432L
        val nome = "Carlos"
        val email = "carlos@carlos.com.br"
        val dataNascimento = LocalDate.of(1982, 10, 10)
        val celular = "(19) 99999-7777"
        val cpf = "350.518.412-87"
        val tipoPessoaFisica = TipoPessoaFisica.ESTUDANTE
        val pessoaFisica = PessoaFisica(nome, email, celular, dataNascimento, cpf, tipoPessoaFisica)
        pessoaFisica.id = id

        val endereco = Endereco("Rua Sem Nome", "123", null, "12.345-678", "Nome do Bairro", "SP", "Campinas")
        pessoaFisica.endereco = endereco

        `when`(pessoaFisicaRepository.get(id)).thenReturn(pessoaFisica)

        val novoCelular = "(19) 99999-8888"
        val novoEmail = "carlos@corp.com"
        val novoTipoPessoaFisica = TipoPessoaFisica.PROFISSIONAL

        val pessoaFisicaParaAtualizar = PessoaFisica(nome, email, celular, dataNascimento, cpf, tipoPessoaFisica)
        pessoaFisicaParaAtualizar.id = id

        val enderecoParaAtualizar = Endereco("Rua Sem Nome", "123", null, "12.345-678", "Nome do Bairro", "SP", "Campinas")
        pessoaFisicaParaAtualizar.endereco = enderecoParaAtualizar

        pessoaFisicaParaAtualizar.celular = novoCelular
        pessoaFisicaParaAtualizar.email = novoEmail
        pessoaFisicaParaAtualizar.tipoPessoaFisica = novoTipoPessoaFisica

        pessoaFisicaService.atualizar(pessoaFisicaParaAtualizar)

        val captor = argumentCaptor<PessoaFisica>()
        verify(pessoaFisicaRepository).atualizar(captor.capture())

        val pessoaFisicaActual = captor.firstValue
        assertThat(pessoaFisicaActual.nome).isEqualToIgnoringCase(pessoaFisicaParaAtualizar.nome)
        assertThat(pessoaFisicaActual.email).isEqualToIgnoringCase(pessoaFisicaParaAtualizar.email)
        assertThat(pessoaFisicaActual.celular).isEqualToIgnoringCase(pessoaFisicaParaAtualizar.celular)
        assertThat(pessoaFisicaActual.tipoPessoaFisica).isEqualTo(pessoaFisicaParaAtualizar.tipoPessoaFisica)
    }
}
