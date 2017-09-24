package br.com.cdcorp.eventos.infrastructure.constraints.validator

import br.com.caelum.stella.validation.CPFValidator
import br.com.caelum.stella.validation.InvalidStateException
import br.com.cdcorp.eventos.infrastructure.constraints.Cpf
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class CpfConstraintValidator : ConstraintValidator<Cpf, String> {

    private var formatted: Boolean = false
    private var required: Boolean = false

    override fun initialize(constraint: Cpf) {
        formatted = constraint.isFormatted
        required = constraint.required
    }

    override fun isValid(value: String, context: ConstraintValidatorContext?): Boolean {

        value.let {
            val cpfValidator = CPFValidator(formatted, true)

            try {
                cpfValidator.assertValid(value.trim())
            } catch (e: InvalidStateException) {
                return false
            }

            return true
        }

        return true
    }
}
