package br.com.cdcorp.eventos.support

import org.junit.runner.RunWith
import org.powermock.modules.junit4.PowerMockRunner
import org.powermock.modules.junit4.PowerMockRunnerDelegate
import org.tools4j.spockito.Spockito

/**
 * Created by ceb on 02/07/17.
 */
@RunWith(PowerMockRunner::class)
@PowerMockRunnerDelegate(value = Spockito::class)
abstract class UnitTest {

}
