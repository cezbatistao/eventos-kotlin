package br.com.cdcorp.eventos.api

import br.com.cdcorp.eventos.api.response.EventosResponse
import br.com.cdcorp.eventos.api.response.eventosResponse
import br.com.cdcorp.eventos.domain.model.PessoaFisica
import br.com.cdcorp.eventos.infrastructure.constraints.Cpf
import br.com.cdcorp.eventos.service.PessoaFisicaService
import org.hibernate.validator.constraints.Email
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid
import javax.validation.constraints.NotNull

/**
 * Created by ceb on 02/07/17.
 */
@RestController
@RequestMapping("/pessoas-fisicas")
@Validated
class PessoaFisicaController {

    private var pessoaFisicaService: PessoaFisicaService

    @Autowired
    constructor(pessoaFisicaService: PessoaFisicaService) {
        this.pessoaFisicaService = pessoaFisicaService
    }

    @GetMapping("/")
    fun todos() : ResponseEntity<EventosResponse> {
        val listaPessoasFisicas: List<PessoaFisica> = pessoaFisicaService.listar()

        var eventosResponse: EventosResponse

        if(listaPessoasFisicas.size == 0) {
            eventosResponse = eventosResponse {
                status {
                    200
                }
                code { "lista_vazia" }
                message { "Resultado sem registros." }
                data { mutableListOf<PessoaFisica>() }
            }
        } else {
            eventosResponse = eventosResponse {
                status {
                    200
                }
                code { "lista_com_resultado" }
                message { "Resultado da pesquisa com Pessoas Fisicas com resultado." }
                data { listaPessoasFisicas }
            }
        }

        return ResponseEntity.ok(eventosResponse);
    }

    @GetMapping("/{cpf}")
    fun porCpf(@Valid @Cpf @PathVariable cpf: String) : ResponseEntity<EventosResponse> {
        val pessoaFisica = pessoaFisicaService.findByCpf(cpf);

        var eventosResponse: EventosResponse = eventosResponse {
            status {
                200
            }
            code { "pessoa_fisica_com_cpf_nao_encontrado" }
            message { "Pessoa física com CPF [$cpf] não encontrado." }
        }

        pessoaFisica?.let {
            eventosResponse = eventosResponse {
                status {
                    200
                }
                code { "pessoa_fisica_com_cpf_encontrado" }
                message { "Pessoa física com CPF [$cpf] encontrado com sucesso." }
                data { pessoaFisica }
            }
        }

        return ResponseEntity.ok(eventosResponse);
    }
}
