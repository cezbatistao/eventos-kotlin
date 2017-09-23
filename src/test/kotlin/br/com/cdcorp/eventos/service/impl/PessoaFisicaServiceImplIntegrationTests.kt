package br.com.cdcorp.eventos.service.impl

import br.com.cdcorp.eventos.domain.model.Endereco
import br.com.cdcorp.eventos.domain.model.PessoaFisica
import br.com.cdcorp.eventos.domain.model.TipoPessoaFisica
import br.com.cdcorp.eventos.service.PessoaFisicaService
import br.com.cdcorp.eventos.support.IntegrationTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate

import org.assertj.core.api.Assertions.*

/**
 * Created by ceb on 12/07/17.
 */
open class PessoaFisicaServiceImplIntegrationTests : IntegrationTest() {

    @Autowired
    private lateinit var pessoaFisicaService: PessoaFisicaService

    @Test
    fun `Criar uma Pessoa Fisica com dados válidos`() {
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

        val pessoaFisicaCadastrada = pessoaFisicaService.findByCpf(cpf)

        assertThat(pessoaFisicaCadastrada).isNotNull()
        assertThat(pessoaFisicaCadastrada!!.id).isNotNull()
        assertThat(pessoaFisicaCadastrada.nome).isEqualTo(nome)
        assertThat(pessoaFisicaCadastrada.email).isEqualTo(email)
        assertThat(pessoaFisicaCadastrada.celular).isEqualTo(celular)
        assertThat(pessoaFisicaCadastrada.telefone).isEqualTo(telefone)
        assertThat(pessoaFisicaCadastrada.dataNascimento).isEqualTo(dataNascimento)
        assertThat(pessoaFisicaCadastrada.cpf).isEqualTo(cpf)
        assertThat(pessoaFisicaCadastrada.rg).isEqualTo(rg)
        assertThat(pessoaFisicaCadastrada.tipoPessoaFisica).isEqualTo(tipoPessoaFisica)
        assertThat(pessoaFisicaCadastrada.endereco).isNotNull()
        assertThat(pessoaFisicaCadastrada.endereco!!.logradouro).isEqualTo(endereco.logradouro)
        assertThat(pessoaFisicaCadastrada.endereco!!.numero).isEqualTo(endereco.numero)
        assertThat(pessoaFisicaCadastrada.endereco!!.complemento).isEqualTo(endereco.complemento)
        assertThat(pessoaFisicaCadastrada.endereco!!.cep).isEqualTo(endereco.cep)
        assertThat(pessoaFisicaCadastrada.endereco!!.bairro).isEqualTo(endereco.bairro)
        assertThat(pessoaFisicaCadastrada.endereco!!.estado).isEqualTo(endereco.estado)
        assertThat(pessoaFisicaCadastrada.endereco!!.cidade).isEqualTo(endereco.cidade)
        assertThat(pessoaFisicaCadastrada.login).isNotNull()
        assertThat(pessoaFisicaCadastrada.login!!.id).isNotNull()
        assertThat(pessoaFisicaCadastrada.login!!.login).isEqualTo(email)
        assertThat(pessoaFisicaCadastrada.login!!.senha).isNotEmpty()
        assertThat(pessoaFisicaCadastrada.login!!.ativo).isTrue()
        assertThat(pessoaFisicaCadastrada.login!!.cadastroConfirmado).isFalse()
    }

    @Test
    fun `Criar uma Pessoa Fisica com dados válidos e não informando os não obrigatórios`() {
        val nome = "Carlos"
        val email = "carlos@carlos.com.br"
        val dataNascimento = LocalDate.of(1982, 10, 10)
        val celular = "(19) 99999-7777"
        val cpf = "350.518.412-87"
        val tipoPessoaFisica = TipoPessoaFisica.ESTUDANTE

        val endereco = Endereco("Rua Sem Nome", "123", null, "12.345-678", "Nome do Bairro", "SP", "Campinas")
        val pessoaFisica = PessoaFisica(nome, email, celular, dataNascimento, cpf, tipoPessoaFisica)
        pessoaFisica.endereco = endereco

        pessoaFisicaService.salvar(pessoaFisica)

        val pessoaFisicaCadastrada = pessoaFisicaService.findByCpf(cpf)

        assertThat(pessoaFisicaCadastrada).isNotNull()
        assertThat(pessoaFisicaCadastrada!!.id).isNotNull()
        assertThat(pessoaFisicaCadastrada.nome).isEqualTo(nome)
        assertThat(pessoaFisicaCadastrada.email).isEqualTo(email)
        assertThat(pessoaFisicaCadastrada.celular).isEqualTo(celular)
        assertThat(pessoaFisicaCadastrada.telefone).isNull()
        assertThat(pessoaFisicaCadastrada.dataNascimento).isEqualTo(dataNascimento)
        assertThat(pessoaFisicaCadastrada.cpf).isEqualTo(cpf)
        assertThat(pessoaFisicaCadastrada.rg).isNull()
        assertThat(pessoaFisicaCadastrada.tipoPessoaFisica).isEqualTo(tipoPessoaFisica)
        assertThat(pessoaFisicaCadastrada.endereco).isNotNull()
        assertThat(pessoaFisicaCadastrada.endereco!!.logradouro).isEqualTo(endereco.logradouro)
        assertThat(pessoaFisicaCadastrada.endereco!!.numero).isEqualTo(endereco.numero)
        assertThat(pessoaFisicaCadastrada.endereco!!.complemento).isNull()
        assertThat(pessoaFisicaCadastrada.endereco!!.cep).isEqualTo(endereco.cep)
        assertThat(pessoaFisicaCadastrada.endereco!!.bairro).isEqualTo(endereco.bairro)
        assertThat(pessoaFisicaCadastrada.endereco!!.estado).isEqualTo(endereco.estado)
        assertThat(pessoaFisicaCadastrada.endereco!!.cidade).isEqualTo(endereco.cidade)
        assertThat(pessoaFisicaCadastrada.login).isNotNull()
        assertThat(pessoaFisicaCadastrada.login!!.id).isNotNull()
        assertThat(pessoaFisicaCadastrada.login!!.login).isEqualTo(email)
        assertThat(pessoaFisicaCadastrada.login!!.senha).isNotEmpty()
        assertThat(pessoaFisicaCadastrada.login!!.ativo).isTrue()
        assertThat(pessoaFisicaCadastrada.login!!.cadastroConfirmado).isFalse()
    }

    @Test
    fun `Atualizar os dados de uma Pessoa Fisica com dados válidos e não informando os não obrigatórios`() {
        val nome = "Carlos"
        val email = "carlos@carlos.com.br"
        val dataNascimento = LocalDate.of(1982, 10, 10)
        val celular = "(19) 99999-7777"
        val cpf = "350.518.412-87"
        val tipoPessoaFisica = TipoPessoaFisica.ESTUDANTE

        val endereco = Endereco("Rua Sem Nome", "123", null, "12.345-678", "Nome do Bairro", "SP", "Campinas")
        val pessoaFisica = PessoaFisica(nome, email, celular, dataNascimento, cpf, tipoPessoaFisica)
        pessoaFisica.endereco = endereco

        pessoaFisicaService.salvar(pessoaFisica)
        val pessoaFisicaCadastrada = pessoaFisicaService.findByCpf(cpf)

        val novoCelular = "(19) 99999-8888"
        val novoEmail = "carlos@corp.com"
        val novoTipoPessoaFisica = TipoPessoaFisica.PROFISSIONAL

        pessoaFisicaCadastrada!!.celular = novoCelular
        pessoaFisicaCadastrada.email = novoEmail
        pessoaFisicaCadastrada.tipoPessoaFisica = novoTipoPessoaFisica

        pessoaFisicaService.atualizar(pessoaFisicaCadastrada)

        val sqlCount = "select count(*) from pessoa_fisica"
        val totalPessoasFisicasCadastradas = jdbcTemplate.queryForObject(
                sqlCount, Integer::class.java)

        assertThat(totalPessoasFisicasCadastradas).isEqualTo(1)

        val pessoaFisicaAtualizado = pessoaFisicaService.findByCpf(cpf)

        assertThat(pessoaFisicaAtualizado).isNotNull()
        assertThat(pessoaFisicaAtualizado!!.id).isNotNull()
        assertThat(pessoaFisicaAtualizado.nome).isEqualTo(nome)
        assertThat(pessoaFisicaAtualizado.email).isEqualTo(novoEmail)
        assertThat(pessoaFisicaAtualizado.celular).isEqualTo(novoCelular)
        assertThat(pessoaFisicaAtualizado.telefone).isNull()
        assertThat(pessoaFisicaAtualizado.dataNascimento).isEqualTo(dataNascimento)
        assertThat(pessoaFisicaAtualizado.cpf).isEqualTo(cpf)
        assertThat(pessoaFisicaAtualizado.rg).isNull()
        assertThat(pessoaFisicaAtualizado.tipoPessoaFisica).isEqualTo(novoTipoPessoaFisica)
        assertThat(pessoaFisicaAtualizado.endereco).isNotNull()
        assertThat(pessoaFisicaAtualizado.endereco!!.logradouro).isEqualTo(endereco.logradouro)
        assertThat(pessoaFisicaAtualizado.endereco!!.numero).isEqualTo(endereco.numero)
        assertThat(pessoaFisicaAtualizado.endereco!!.complemento).isNull()
        assertThat(pessoaFisicaAtualizado.endereco!!.cep).isEqualTo(endereco.cep)
        assertThat(pessoaFisicaAtualizado.endereco!!.bairro).isEqualTo(endereco.bairro)
        assertThat(pessoaFisicaAtualizado.endereco!!.estado).isEqualTo(endereco.estado)
        assertThat(pessoaFisicaAtualizado.endereco!!.cidade).isEqualTo(endereco.cidade)
        assertThat(pessoaFisicaAtualizado.login).isNotNull()
        assertThat(pessoaFisicaAtualizado.login!!.id).isNotNull()
        assertThat(pessoaFisicaAtualizado.login!!.login).isEqualTo(email)
        assertThat(pessoaFisicaAtualizado.login!!.senha).isNotEmpty()
        assertThat(pessoaFisicaAtualizado.login!!.ativo).isTrue()
        assertThat(pessoaFisicaAtualizado.login!!.cadastroConfirmado).isFalse()
    }
}