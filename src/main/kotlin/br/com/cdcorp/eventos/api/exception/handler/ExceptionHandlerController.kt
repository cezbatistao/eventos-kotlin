package br.com.cdcorp.eventos.api.exception.handler

import br.com.cdcorp.eventos.api.response.EventosResponse
import br.com.cdcorp.eventos.api.response.eventosResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import javax.validation.ConstraintViolationException

@ControllerAdvice
class ExceptionHandlerController {

    @ResponseBody
    @ExceptionHandler
    fun defaultErrorHandler(e: ConstraintViolationException): ResponseEntity<EventosResponse> {
        val errors: MutableList<br.com.cdcorp.eventos.api.response.Error> = mutableListOf()

        e.constraintViolations.forEach {
            val error: br.com.cdcorp.eventos.api.response.Error = br.com.cdcorp.eventos.api.response.error {
                code { it.propertyPath.last().name }
                message { it.message }
            }

            errors.add(error)
        }

        val eventosResponse: EventosResponse = eventosResponse {
            status {
                400
            }
            code { "erro_de_validacao" }
            message { "Erro ao validar uma propriedade." }
            errors { errors }
        }

        return ResponseEntity.badRequest().body(eventosResponse)
    }
}
