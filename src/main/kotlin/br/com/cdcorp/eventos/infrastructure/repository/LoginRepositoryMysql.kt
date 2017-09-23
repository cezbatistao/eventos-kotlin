package br.com.cdcorp.eventos.infrastructure.repository

import br.com.cdcorp.eventos.domain.model.Login
import br.com.cdcorp.eventos.domain.repository.LoginRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.sql.ResultSet

/**
 * Created by ceb on 12/07/17.
 */
@Repository
class LoginRepositoryMysql : LoginRepository {

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    override fun findByEmail(email: String): Login? {
        val sql = "SELECT id, login, senha, ativo, cadastro_confirmado FROM login where login = ?"
        val logins: List<Login> = jdbcTemplate.query(sql, arrayOf(email), LoginRowMapper())

        return if (logins.isNotEmpty()) logins[0] else null
    }

    override fun salvar(login: Login): Login {
        val sql = "INSERT INTO login(login, senha, ativo, cadastro_confirmado) VALUES(?, ?, ?, ?)"
        jdbcTemplate.update(sql, {stmt->
            stmt.setString(1, login.login)
            stmt.setString(2, login.senha)
            stmt.setBoolean(3, login.ativo)
            stmt.setBoolean(4, login.cadastroConfirmado)
        })

        return this.findByEmail(login.login)!!
    }

    override fun get(id: Long): Login? {
        val sql = "SELECT id, login, senha, ativo, cadastro_confirmado FROM login where id = ?"
        val logins: List<Login> = jdbcTemplate.query(sql, arrayOf(id), LoginRowMapper())

        return if (logins.isNotEmpty()) logins[0] else null
    }

    class LoginRowMapper : RowMapper<Login> {
        override fun mapRow(rs: ResultSet, rowNum: Int): Login {
            var login = Login(rs.getString("login"), rs.getString("senha"))
            login.id = rs.getLong("id")
            login.ativo = rs.getBoolean("ativo")
            login.cadastroConfirmado = rs.getBoolean("cadastro_confirmado")

            return login
        }
    }
}
