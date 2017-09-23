package br.com.cdcorp.eventos.support

import br.com.cdcorp.eventos.infrastructure.config.EventosApplication
import liquibase.Liquibase
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.annotation.Transactional

/**
 * Created by ceb on 02/07/17.
 */
@Transactional
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = arrayOf(EventosApplication::class))
@ActiveProfiles("test")
@TestPropertySource(locations = arrayOf("classpath:application-test.properties"))
abstract class IntegrationTest {

    @Autowired
    internal lateinit var jdbcTemplate: JdbcTemplate

    @After
    fun cleanup() {
        jdbcTemplate.execute("DELETE FROM login;")
        jdbcTemplate.execute("DELETE FROM pessoa_fisica;")
        jdbcTemplate.execute("COMMIT;")
    }
}
