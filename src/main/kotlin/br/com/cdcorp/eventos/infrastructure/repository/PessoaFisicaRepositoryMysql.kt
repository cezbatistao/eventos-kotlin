package br.com.cdcorp.eventos.infrastructure.repository

import br.com.cdcorp.eventos.domain.model.Endereco
import br.com.cdcorp.eventos.domain.model.PessoaFisica
import br.com.cdcorp.eventos.domain.model.TipoPessoaFisica
import br.com.cdcorp.eventos.domain.repository.LoginRepository
import br.com.cdcorp.eventos.domain.repository.PessoaFisicaRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.sql.Date
import java.sql.ResultSet
import java.time.LocalDate

/**
 * Created by ceb on 12/07/17.
 */
@Repository
class PessoaFisicaRepositoryMysql : PessoaFisicaRepository {

    private var jdbcTemplate: JdbcTemplate
    private var loginRepository: LoginRepository

    @Autowired
    constructor(jdbcTemplate: JdbcTemplate, loginRepository: LoginRepository) {
        this.jdbcTemplate = jdbcTemplate
        this.loginRepository = loginRepository
    }

    override fun create(pessoaFisica: PessoaFisica): PessoaFisica {
        val sql = "INSERT INTO pessoa_fisica(nome, email, celular, telefone, data_nascimento, cpf, rg, tipo_pessoa, endereco_logradouro, " +
                "endereco_numero, endereco_complemento, endereco_cep, endereco_bairro, endereco_estado, endereco_cidade, " +
                "login_id) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
        jdbcTemplate.update(sql, {stmt->
            val dataNascimento: Date = Date.valueOf(pessoaFisica.dataNascimento)

            stmt.setString(1, pessoaFisica.nome)
            stmt.setString(2, pessoaFisica.email)
            stmt.setString(3, pessoaFisica.celular)
            stmt.setString(4, pessoaFisica.telefone)
            stmt.setDate(5, dataNascimento)
            stmt.setString(6, pessoaFisica.cpf)
            stmt.setString(7, pessoaFisica.rg)
            stmt.setString(8, pessoaFisica.tipoPessoaFisica.name)

            val endereco = pessoaFisica.endereco!!

            stmt.setString(9, endereco.logradouro)
            stmt.setString(10, endereco.numero)
            stmt.setString(11, endereco.complemento)
            stmt.setString(12, endereco.cep)
            stmt.setString(13, endereco.bairro)
            stmt.setString(14, endereco.estado)
            stmt.setString(15, endereco.cidade)

            stmt.setLong(16, pessoaFisica.login?.id!!)
        })

        return this.findByCpf(pessoaFisica.cpf)!!
    }

    override fun findByCpf(cpf: String): PessoaFisica? {
        // TODO pode-se usar a classe ResultSetExtractor para retornar
        val sql = "SELECT id, nome, email, celular, telefone, data_nascimento, cpf, rg, tipo_pessoa, endereco_logradouro, endereco_numero, " +
                "endereco_complemento, endereco_cep, endereco_bairro, endereco_estado, endereco_cidade, login_id FROM pessoa_fisica where cpf = ?"
        val pessoasFisicas: List<PessoaFisica> = jdbcTemplate.query(sql, arrayOf(cpf), PessoaFisicaRowMapper(this.loginRepository))

        return if (pessoasFisicas.isNotEmpty()) pessoasFisicas[0] else null
    }

    override fun get(id: Long): PessoaFisica? {
        val sql = "SELECT id, nome, email, celular, telefone, data_nascimento, cpf, rg, tipo_pessoa, endereco_logradouro, endereco_numero, " +
                "endereco_complemento, endereco_cep, endereco_bairro, endereco_estado, endereco_cidade, login_id FROM pessoa_fisica where id = ?"
        val pessoasFisicas: List<PessoaFisica> = jdbcTemplate.query(sql, arrayOf(id), PessoaFisicaRowMapper(this.loginRepository))

        return if (pessoasFisicas.isNotEmpty()) pessoasFisicas[0] else null
    }

    override fun update(pessoaFisica: PessoaFisica): PessoaFisica {
        val sql = "UPDATE pessoa_fisica set nome = ?, email = ?, celular = ?, telefone = ?, rg = ?, tipo_pessoa = ?, endereco_logradouro = ?, " +
                "endereco_numero = ?, endereco_complemento = ?, endereco_cep = ?, endereco_bairro = ?, endereco_estado = ?, " +
                "endereco_cidade = ? WHERE id = ?"
        jdbcTemplate.update(sql, {stmt->
            stmt.setString(1, pessoaFisica.nome)
            stmt.setString(2, pessoaFisica.email)
            stmt.setString(3, pessoaFisica.celular)
            stmt.setString(4, pessoaFisica.telefone)
            stmt.setString(5, pessoaFisica.rg)
            stmt.setString(6, pessoaFisica.tipoPessoaFisica.name)

            val endereco = pessoaFisica.endereco!!

            stmt.setString(7, endereco.logradouro)
            stmt.setString(8, endereco.numero)
            stmt.setString(9, endereco.complemento)
            stmt.setString(10, endereco.cep)
            stmt.setString(11, endereco.bairro)
            stmt.setString(12, endereco.estado)
            stmt.setString(13, endereco.cidade)

            stmt.setLong(14, pessoaFisica.id!!)
        })

        return pessoaFisica
    }

    override fun list(): List<PessoaFisica> {
        val sql = "SELECT id, nome, email, celular, telefone, data_nascimento, cpf, rg, tipo_pessoa, endereco_logradouro, endereco_numero, " +
                "endereco_complemento, endereco_cep, endereco_bairro, endereco_estado, endereco_cidade, login_id FROM pessoa_fisica"

        return jdbcTemplate.query(sql, PessoaFisicaRowMapper(this.loginRepository))
    }

    class PessoaFisicaRowMapper(private var loginRepository: LoginRepository) : RowMapper<PessoaFisica> {

        override fun mapRow(rs: ResultSet, rowNum: Int): PessoaFisica {
            val dataNascimento: LocalDate = rs.getDate("data_nascimento").toLocalDate()
            val tipoPessoaFisica = TipoPessoaFisica.valueOf(rs.getString("tipo_pessoa"))
            val pessoaFisica = PessoaFisica(rs.getString("nome"), rs.getString("email"), rs.getString("celular"), dataNascimento, rs.getString("cpf"),
                    tipoPessoaFisica)
            pessoaFisica.id = rs.getLong("id")
            pessoaFisica.telefone = rs.getString("telefone")
            pessoaFisica.rg = rs.getString("rg")

            val endereco = Endereco(rs.getString("endereco_logradouro"), rs.getString("endereco_numero"), rs.getString("endereco_complemento"),
                    rs.getString("endereco_cep"), rs.getString("endereco_bairro"), rs.getString("endereco_estado"), rs.getString("endereco_cidade"))
            pessoaFisica.endereco = endereco

            val login = this.loginRepository.get(rs.getLong("login_id"))
            pessoaFisica.login = login

            return pessoaFisica
        }
    }
}
