package br.com.cdcorp.eventos.service.impl

import br.com.cdcorp.eventos.domain.model.Login
import br.com.cdcorp.eventos.domain.repository.LoginRepository
import br.com.cdcorp.eventos.infrastructure.encode.CriptografiaSenha
import br.com.cdcorp.eventos.service.ConfiguracaoService
import br.com.cdcorp.eventos.service.LoginService
import br.com.cdcorp.eventos.service.NotificacaoService
import br.com.cdcorp.eventos.service.exception.Notificacao
import com.mitchellbosecke.pebble.PebbleEngine
import com.mitchellbosecke.pebble.loader.StringLoader
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.io.StringWriter
import java.util.*

/**
 * Created by ceb on 06/07/17.
 */
@Service
@Transactional(readOnly = true)
class LoginServiceImpl : LoginService {

    private val notificacaoService: NotificacaoService
    private val loginRepository: LoginRepository

    private val configuracaoService: ConfiguracaoService
    private val criptografiaSenha: CriptografiaSenha

    @Autowired
    constructor(notificacaoService: NotificacaoService, loginRepository: LoginRepository, configuracaoService: ConfiguracaoService,
                criptografiaSenha: CriptografiaSenha) {
        this.notificacaoService = notificacaoService
        this.loginRepository = loginRepository
        this.configuracaoService = configuracaoService
        this.criptografiaSenha = criptografiaSenha
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    override fun criarLoginUsuario(email: String): Login {
        val login = gerarLoginUsuario(email)
        return loginRepository.salvar(login)
    }

    internal fun gerarLoginUsuario(email: String): Login {
        val notificacao: Notificacao = Notificacao()

        validarLogin(notificacao, email)

        if (notificacao.temErro()) {
            notificacao.throwErros();
        }

        val senhaGerada = criptografiaSenha.gerarSenha()

        val assunto = configuracaoService.get("notificacao.loginGerado.assunto")
        val templateMensagem = configuracaoService.get("notificacao.loginGerado.corpo")

        val mensagemDefault = gerarMensagemDeCadastroConcluido(templateMensagem, email, senhaGerada)

        notificacaoService.enviarEmail(email, assunto, mensagemDefault)

        val senhaGeradaCriptografada = criptografiaSenha.criptografar(senhaGerada)

        val login = Login(email, senhaGeradaCriptografada)
        return login
    }

    private fun validarLogin(notificacao: Notificacao, email: String) {
        if (loginRepository.findByEmail(email) != null) notificacao.addErro("email", "registrao_ja_cadastrado", "E-mail ${email} já cadastrado.")
    }

    internal fun gerarMensagemDeCadastroConcluido(templateMensagem: String, email: String, senha: String) : String {
        val engine = PebbleEngine.Builder().loader(StringLoader()).build()
        val compiledTemplate = engine.getTemplate(templateMensagem)
        val writer = StringWriter()

        val context = HashMap<String, Any>()
        context.put("login", email)
        context.put("senha", senha)

        compiledTemplate.evaluate(writer, context)
        return writer.toString()
    }

    override fun confirmarCadastro(email: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun desativarUsuario(email: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun trocarSenha(email: String, senha: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}