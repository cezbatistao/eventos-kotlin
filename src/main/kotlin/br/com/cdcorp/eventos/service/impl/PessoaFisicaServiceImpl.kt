package br.com.cdcorp.eventos.service.impl

import br.com.caelum.stella.validation.CPFValidator
import br.com.caelum.stella.validation.InvalidStateException
import br.com.cdcorp.eventos.domain.model.PessoaFisica
import br.com.cdcorp.eventos.domain.repository.PessoaFisicaRepository
import br.com.cdcorp.eventos.service.LoginService
import br.com.cdcorp.eventos.service.NotificacaoService
import br.com.cdcorp.eventos.service.PessoaFisicaService
import br.com.cdcorp.eventos.service.exception.Notificacao
import org.apache.commons.lang3.StringUtils.isBlank
import org.apache.commons.validator.routines.EmailValidator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.temporal.ChronoUnit

/**
 * Created by ceb on 02/07/17.
 */
@Service
@Transactional(readOnly = true)
class PessoaFisicaServiceImpl : PessoaFisicaService {

    private var loginService: LoginService
    private var notificacaoService: NotificacaoService
    private var pessoaFisicaRepository: PessoaFisicaRepository

    @Autowired
    constructor(loginService: LoginService, notificacaoService: NotificacaoService, pessoaFisicaRepository: PessoaFisicaRepository) {
        this.loginService = loginService
        this.notificacaoService = notificacaoService
        this.pessoaFisicaRepository = pessoaFisicaRepository
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    override fun salvar(pessoaFisica: PessoaFisica) : PessoaFisica {
        val notificacao = Notificacao()
        validarPessoaFisica(notificacao, pessoaFisica)

        if (notificacao.temErro()) {
            notificacao.throwErros()
        }

        val login = loginService.criarLoginUsuario(pessoaFisica.email)
        pessoaFisica.login = login

        return pessoaFisicaRepository.create(pessoaFisica)
    }

    override fun atualizar(pessoaFisica: PessoaFisica) : PessoaFisica {
        val notificacao: Notificacao = Notificacao()

        if(pessoaFisica.id == null) notificacao.addErro("nome", "null", "ID é obrigatório para atualizar.")

        validarPessoaFisica(notificacao, pessoaFisica)

        var pessoaFisicaSalva: PessoaFisica? = null
        if(pessoaFisica.id != null) {
            pessoaFisicaSalva = pessoaFisicaRepository.get(pessoaFisica.id!!)
            if(pessoaFisicaSalva == null) notificacao.addErro("pessoa_fisica_nao_cadastrada", "Não existe cadastro de Pessoa Fisica com o ID ${pessoaFisica.id}.")
        }

        if(notificacao.temErro()) {
            notificacao.throwErros();
        }

        pessoaFisicaSalva!!.nome = pessoaFisica.nome
        pessoaFisicaSalva.email = pessoaFisica.email
        pessoaFisicaSalva.celular = pessoaFisica.celular
        pessoaFisicaSalva.telefone = pessoaFisica.telefone
        pessoaFisicaSalva.endereco = pessoaFisica.endereco
        pessoaFisicaSalva.tipoPessoaFisica = pessoaFisica.tipoPessoaFisica
        pessoaFisicaSalva.rg = pessoaFisica.rg

        return pessoaFisicaRepository.update(pessoaFisicaSalva)
    }

    override fun findByCpf(cpf: String): PessoaFisica? {
        return pessoaFisicaRepository.findByCpf(cpf)
    }

    override fun listar(): List<PessoaFisica> {
        return pessoaFisicaRepository.list()
    }

    private fun validarPessoaFisica(notificacao: Notificacao, pessoaFisica: PessoaFisica) {
        if (isBlank(pessoaFisica.nome)) notificacao.addErro("nome", "nulo_branco", "Nome é obrigatório.")

        val emailValidator = EmailValidator.getInstance(false)
        if (isBlank(pessoaFisica.email)) {
            notificacao.addErro("email", "nulo_branco", "E-mail é obrigatório.")
        } else if (!emailValidator.isValid(pessoaFisica.email.trim())) {
            notificacao.addErro("email", "email_invalid", "E-mail inválido.")
        }

        if (isBlank(pessoaFisica.celular)) {
            notificacao.addErro("celular", "nulo_branco", "Celular é obrigatório.")
        } else if (!pessoaFisica.celular.trim().matches(Regex(".((10)|([1-9][1-9]).)\\s9?[6-9][0-9]{3}-[0-9]{4}"))) {
            notificacao.addErro("celular", "cell_invalid", "Celular inválido.")
        }

        if (pessoaFisica.dataNascimento == null) {
            notificacao.addErro("dataNascimento", "nulo_branco", "Data de nascimento é obrigatório.")
        } else {
            val now = LocalDate.now()
            val idade = ChronoUnit.YEARS.between(pessoaFisica.dataNascimento, now)
            val idadeMinima = 18
            if (idadeMinima > idade) {
                notificacao.addErro("dataNascimento", "dataNascimento_invalid", "Idade mínima para participar é de ${idadeMinima} anos.")
            }
        }

        val cpfValidator = CPFValidator(true, true)
        if (isBlank(pessoaFisica.cpf)) {
            notificacao.addErro("cpf", "nulo_branco", "CPF é obrigatório.")
        } else {
            try {
                cpfValidator.assertValid(pessoaFisica.cpf.trim())
            } catch (e: InvalidStateException) {
                notificacao.addErro("cpf", "cpf_invalid", "CPF inválido.")
            }
        }

        if(pessoaFisica.endereco == null) {
            notificacao.addErro("endereco", "endereco_pessoa_fisica", "Endereço é obrigatório.")
        } else {
            val endereco = pessoaFisica.endereco
            if(isBlank(endereco!!.logradouro)) notificacao.addErro("logradouro", "endereco_pessoa_fisica", "Logradouro é obrigatório.")
            if(isBlank(endereco.numero)) notificacao.addErro("numero", "endereco_pessoa_fisica", "Número é obrigatório.")
            if(isBlank(endereco.cep)) notificacao.addErro("cep", "endereco_pessoa_fisica", "CEP é obrigatório.")
            if(isBlank(endereco.bairro)) notificacao.addErro("bairro", "endereco_pessoa_fisica", "Bairro é obrigatório.")
            if(isBlank(endereco.estado)) notificacao.addErro("estado", "endereco_pessoa_fisica", "Estado é obrigatório.")
            if(isBlank(endereco.cidade)) notificacao.addErro("cidade", "endereco_pessoa_fisica", "Cidade é obrigatório.")
        }

        val pessoaFisicaJaCadastrada = pessoaFisicaRepository.findByCpf(pessoaFisica.cpf)
        if (pessoaFisicaJaCadastrada != null && (pessoaFisica.id == null || !pessoaFisicaJaCadastrada.id?.equals(pessoaFisica.id)!!)) notificacao.addErro("pessoa_ja_cadastrada", "Pessoa física com CPF ${pessoaFisica.cpf} já cadastrado.")
    }
}
