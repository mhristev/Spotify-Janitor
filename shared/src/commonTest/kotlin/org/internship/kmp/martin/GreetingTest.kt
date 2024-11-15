import kotlinx.coroutines.runBlocking
import org.internship.kmp.martin.Greeting
import kotlin.test.Test
import kotlin.test.assertEquals

class GreetingTest {

    @Test
    fun greet() = runBlocking {
        val greeting = Greeting()
        val result = greeting.greet()
        assertEquals("Expected response body", result)
    }
}