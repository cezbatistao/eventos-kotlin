package br.com.cdcorp.eventos.infrastructure.constraints

import br.com.cdcorp.eventos.infrastructure.constraints.validator.CpfConstraintValidator
import java.lang.annotation.Documented
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy.RUNTIME
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass


@Documented
@Constraint(validatedBy = arrayOf(CpfConstraintValidator::class))
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPE, AnnotationTarget.TYPE_PARAMETER)
@Retention(RUNTIME)
annotation class Cpf(
        val message: String = "{br.com.cdcorp.eventos.infrastructure.constraints.Cpf.message}",
        val groups: Array<KClass<*>> = arrayOf(),
        val payload: Array<KClass<out Payload>> = arrayOf(),
        val isFormatted: Boolean = false,
        val required: Boolean = false
)
