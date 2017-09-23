package br.com.cdcorp.eventos.service.exception

/**
 * Created by ceb on 02/07/17.
 */
abstract class EventosException : RuntimeException {
    constructor(message: String, ex: Exception?): super(message, ex) {}
    constructor(message: String): super(message) {}
    constructor(ex: Exception): super(ex) {}
}