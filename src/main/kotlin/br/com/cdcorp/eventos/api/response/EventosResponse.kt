package br.com.cdcorp.eventos.api.response

import br.com.cdcorp.eventos.domain.model.PessoaFisica

class EventosResponse {

    var status: Int? = null
        internal set
    var code: String? = null
        internal set
    var message: String? = null
        internal set
    var errors: List<Error>? = null
        internal set
    var data: Any? = null
        internal set

}

//Builder: EventosResponse
class EventosResponseBuilder() {
    constructor(init: EventosResponseBuilder.() -> Unit) : this() {
        init()
    }

    private var status: Int? = null
    private var code: String? = null
    private var message: String? = null
    private var errors: List<Error>? = null
    private var data: Any? = null

    fun status(init: () -> Int) {
        status = init()
    }

    fun code(init: () -> String) {
        code = init()
    }

    fun message(init: () -> String) {
        message = init()
    }

    fun errors(init: () -> List<Error>) {
        errors = init()
    }

    fun data(init: () -> Any) {
        data = init()
    }

    fun build(): EventosResponse {
        val eventosResponse = EventosResponse()

        status.apply {
            eventosResponse.status = status
        }

        code.apply {
            eventosResponse.code = code
        }

        message.apply {
            eventosResponse.message = message
        }

        errors?.apply {
            eventosResponse.errors = errors
        }

        data?.apply {
            eventosResponse.data = data
        }

        return eventosResponse
    }

}

fun eventosResponse(init: EventosResponseBuilder.() -> Unit): EventosResponse {
    return EventosResponseBuilder(init).build()
}

class Error {

    var code: String? = null
        internal set
    var message: String? = null
        internal set
}

//Builder: EventosResponse
class ErrorBuilder() {
    constructor(init: ErrorBuilder.() -> Unit) : this() {
        init()
    }

    private var code: String? = null
    private var message: String? = null

    fun code(init: () -> String) {
        code = init()
    }

    fun message(init: () -> String) {
        message = init()
    }

    fun build(): Error {
        val error = Error()

        code.apply {
            error.code = code
        }

        message.apply {
            error.message = message
        }

        return error
    }
}

fun error(init: ErrorBuilder.() -> Unit): Error {
    return ErrorBuilder(init).build()
}
