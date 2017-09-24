package br.com.cdcorp.eventos.api

import br.com.cdcorp.eventos.api.response.EventosResponse
import br.com.cdcorp.eventos.domain.model.Endereco
import br.com.cdcorp.eventos.domain.model.PessoaFisica
import br.com.cdcorp.eventos.domain.model.TipoPessoaFisica
import br.com.cdcorp.eventos.domain.repository.LoginRepository
import br.com.cdcorp.eventos.infrastructure.encode.CriptografiaSenha
import br.com.cdcorp.eventos.service.ConfiguracaoService
import br.com.cdcorp.eventos.service.NotificacaoService
import br.com.cdcorp.eventos.service.PessoaFisicaService
import br.com.cdcorp.eventos.service.impl.LoginServiceImpl
import br.com.cdcorp.eventos.support.UnitTest
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.hasSize
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.assertj.core.api.Assertions
import org.hamcrest.Matchers
import org.hamcrest.core.AllOf
import org.junit.Before
import org.junit.Test
import org.powermock.api.mockito.PowerMockito
import org.springframework.http.ResponseEntity
import java.time.LocalDate

class PessoaFisicaControllerUnitTests : UnitTest() {

    private lateinit var pessoaFisicaService : PessoaFisicaService
    private lateinit var pessoaFisicaController : PessoaFisicaController

    @Before
    fun setup() {
        pessoaFisicaService = mock<PessoaFisicaService>()
        pessoaFisicaController = PessoaFisicaController(pessoaFisicaService)
    }

    @Test
    fun `Teste endpoint para retornar uma lista de Pessoas Fisicas encontrando os registros`() {
        val nome = "Carlos"
        val email = "carlos@carlos.com.br"
        val dataNascimento = LocalDate.of(1982, 10, 10)
        val celular = "(19) 99999-7777"
        val cpf = "350.518.412-87"
        val tipoPessoaFisica = TipoPessoaFisica.ESTUDANTE

        val endereco = Endereco("Rua Sem Nome", "123", null, "12.345-678", "Nome do Bairro", "SP", "Campinas")
        val pessoaFisica = PessoaFisica(nome, email, celular, dataNascimento, cpf, tipoPessoaFisica)
        pessoaFisica.id = 3232L
        pessoaFisica.endereco = endereco

        whenever(pessoaFisicaService.listar()).thenReturn(arrayListOf(pessoaFisica))

        val responseActual: ResponseEntity<EventosResponse> = pessoaFisicaController.todos()

        Assertions.assertThat(responseActual).isNotNull()
        Assertions.assertThat(responseActual.statusCodeValue).isEqualTo(200)

        val eventosResponseActual: EventosResponse = responseActual.body

        Assertions.assertThat(eventosResponseActual.status).isEqualTo(200)
        Assertions.assertThat(eventosResponseActual.code).isEqualTo("lista_com_resultado")
        Assertions.assertThat(eventosResponseActual.message).isEqualTo("Resultado da pesquisa com Pessoas Fisicas com resultado.")
        Assertions.assertThat(eventosResponseActual.data).isNotNull()

        val listaPessoasFisicias: List<PessoaFisica> = eventosResponseActual.data as List<PessoaFisica>

        Assertions.assertThat(listaPessoasFisicias.size).isEqualTo(1)
    }

    @Test
    fun `Teste endpoint para retornar uma lista de Pessoas Fisicas NAO encontrando os registros`() {
        whenever(pessoaFisicaService.listar()).thenReturn(arrayListOf())

        val responseActual: ResponseEntity<EventosResponse> = pessoaFisicaController.todos()

        Assertions.assertThat(responseActual).isNotNull()
        Assertions.assertThat(responseActual.statusCodeValue).isEqualTo(200)

        val eventosResponseActual: EventosResponse = responseActual.body

        Assertions.assertThat(eventosResponseActual.status).isEqualTo(200)
        Assertions.assertThat(eventosResponseActual.code).isEqualTo("lista_vazia")
        Assertions.assertThat(eventosResponseActual.message).isEqualTo("Resultado sem registros.")
        Assertions.assertThat(eventosResponseActual.data).isNotNull()

        val listaPessoasFisicias: List<PessoaFisica> = eventosResponseActual.data as List<PessoaFisica>

        Assertions.assertThat(listaPessoasFisicias.size).isEqualTo(0)
    }

    @Test
    fun `Recuperar uma Pessoa Fisica informando um CPF com resultado`() {
        val nome = "Carlos"
        val email = "carlos@carlos.com.br"
        val dataNascimento = LocalDate.of(1982, 10, 10)
        val celular = "(19) 99999-7777"
        val cpf = "350.518.412-87"
        val tipoPessoaFisica = TipoPessoaFisica.ESTUDANTE

        val endereco = Endereco("Rua Sem Nome", "123", null, "12.345-678", "Nome do Bairro", "SP", "Campinas")
        val pessoaFisica = PessoaFisica(nome, email, celular, dataNascimento, cpf, tipoPessoaFisica)
        pessoaFisica.id = 3232L
        pessoaFisica.endereco = endereco

        whenever(pessoaFisicaService.findByCpf(cpf)).thenReturn(pessoaFisica)

        val responseActual: ResponseEntity<EventosResponse> = pessoaFisicaController.porCpf(cpf)

        Assertions.assertThat(responseActual).isNotNull()
        Assertions.assertThat(responseActual.statusCodeValue).isEqualTo(200)

        val eventosResponseActual: EventosResponse = responseActual.body

        Assertions.assertThat(eventosResponseActual.status).isEqualTo(200)
        Assertions.assertThat(eventosResponseActual.code).isEqualTo("pessoa_fisica_com_cpf_encontrado")
        Assertions.assertThat(eventosResponseActual.message).isEqualTo("Pessoa física com CPF [$cpf] encontrado com sucesso.")
        Assertions.assertThat(eventosResponseActual.data).isNotNull()
    }

    @Test
    fun `Nao recuperar uma Pessoa Fisica informando um CPF sem resultado`() {
        val cpf = "350.518.412-87"

        whenever(pessoaFisicaService.findByCpf(cpf)).thenReturn(null)

        val responseActual: ResponseEntity<EventosResponse> = pessoaFisicaController.porCpf(cpf)

        Assertions.assertThat(responseActual).isNotNull()
        Assertions.assertThat(responseActual.statusCodeValue).isEqualTo(200)

        val eventosResponseActual: EventosResponse = responseActual.body

        Assertions.assertThat(eventosResponseActual.status).isEqualTo(200)
        Assertions.assertThat(eventosResponseActual.code).isEqualTo("pessoa_fisica_com_cpf_nao_encontrado")
        Assertions.assertThat(eventosResponseActual.message).isEqualTo("Pessoa física com CPF [$cpf] não encontrado.")

        // TODO validar a propriedade errors
    }
}
