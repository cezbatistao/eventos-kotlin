package br.com.cdcorp.eventos.api

import br.com.cdcorp.eventos.domain.model.Endereco
import br.com.cdcorp.eventos.domain.model.PessoaFisica
import br.com.cdcorp.eventos.domain.model.TipoPessoaFisica
import br.com.cdcorp.eventos.service.PessoaFisicaService
import br.com.cdcorp.eventos.support.IntegrationTest
import com.jayway.restassured.RestAssured.`when`
import org.apache.http.HttpStatus
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate
import org.springframework.boot.context.embedded.LocalServerPort
import com.jayway.restassured.RestAssured
import com.jayway.restassured.http.ContentType
import com.jayway.restassured.path.json.JsonPath
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.entry
import org.junit.Before





class PessoaFisicaControllerIntegrationTests : IntegrationTest() {

    @Autowired
    private lateinit var pessoaFisicaService: PessoaFisicaService

    @LocalServerPort
    var port: Int = 0

    @Before
    fun setUp() {
        RestAssured.port = port
    }

    @Test
    fun `Testar o endpoint de obter uma Pessoa Fisica por CPF informando o CPF invalido`() {
        val nome = "Carlos"
        val email = "carlos@carlos.com.br"
        val dataNascimento = LocalDate.of(1982, 10, 10)
        val celular = "(19) 99999-7777"
        val telefone = "(19) 3232-5454"
        val cpf = "350.518.412-87"
        val rg = "12.123.123-9"
        val tipoPessoaFisica = TipoPessoaFisica.ESTUDANTE

        val endereco = Endereco("Rua Sem Nome", "123", "APTO 45", "12.345-678", "Nome do Bairro", "SP", "Campinas")
        val pessoaFisica = PessoaFisica(nome, email, celular, dataNascimento, cpf, tipoPessoaFisica)
        pessoaFisica.endereco = endereco
        pessoaFisica.rg = rg
        pessoaFisica.telefone = telefone

        pessoaFisicaService.salvar(pessoaFisica)

        val cpfToUse = "15051841287"

        val response = `when`()
                .get("/eventos/pessoas-fisicas/${cpfToUse}")
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .extract().response()


        val jsonPath = JsonPath.from(response.asString())

        Assertions.assertThat(jsonPath.get<Int>("status")).isEqualTo(400)
        Assertions.assertThat(jsonPath.get<String>("code")).isEqualTo("erro_de_validacao")
        Assertions.assertThat(jsonPath.get<String>("message")).isEqualTo("Erro ao validar uma propriedade.")

        val erros = jsonPath.getList<Map<String, String>>("errors")

        Assertions.assertThat(erros.size).isEqualTo(1)

        val error: Map<String, String> = erros.get(0)

        Assertions.assertThat(error).containsKey("code")
        Assertions.assertThat(error).containsKey("message")

        Assertions.assertThat(error).contains(entry("code", "cpf"), entry("message", "CPF 15051841287 inv\u00E1lido"));
    }
}