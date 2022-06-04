package nl.com.lucianoluzzi.testresources.extensions

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.extension.*
import kotlin.coroutines.EmptyCoroutineContext

@ExperimentalCoroutinesApi
class TestDispatcherExtension :
    BeforeAllCallback,
    AfterAllCallback,
    ParameterResolver {

    private val dispatcher = UnconfinedTestDispatcher()
    private val coroutineScope = TestScope(EmptyCoroutineContext + dispatcher)

    override fun beforeAll(context: ExtensionContext) {
        Dispatchers.setMain(dispatcher)
    }

    override fun afterAll(context: ExtensionContext) {
        Dispatchers.resetMain()
    }

    override fun supportsParameter(
        parameterContext: ParameterContext,
        extensionContext: ExtensionContext
    ): Boolean =
        parameterContext.parameter?.type == TestScope::class.java

    override fun resolveParameter(
        parameterContext: ParameterContext,
        extensionContext: ExtensionContext
    ): Any = coroutineScope
}
