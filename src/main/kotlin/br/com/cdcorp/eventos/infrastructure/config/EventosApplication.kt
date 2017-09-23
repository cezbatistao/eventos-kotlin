package br.com.cdcorp.eventos.infrastructure.config

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.transaction.annotation.EnableTransactionManagement

/**
 * Created by ceb on 02/07/17.
 */
@EnableWebSecurity
@EnableTransactionManagement
@SpringBootApplication(scanBasePackages = arrayOf("br.com.cdcorp.eventos"))
class EventosApplication {

    fun main(args: Array<String>) {
        SpringApplication.run(EventosApplication::class.java, *args)
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}