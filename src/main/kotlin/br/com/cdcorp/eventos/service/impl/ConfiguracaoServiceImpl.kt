package br.com.cdcorp.eventos.service.impl

import br.com.cdcorp.eventos.service.ConfiguracaoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service

/**
 * Created by ceb on 12/07/17.
 */
@Service
class ConfiguracaoServiceImpl : ConfiguracaoService {

    @Autowired
    private lateinit var env: Environment

    override fun get(propriedade: String): String {
        return env.getProperty(propriedade)
    }
}