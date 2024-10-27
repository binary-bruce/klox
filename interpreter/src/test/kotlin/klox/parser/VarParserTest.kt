package klox.parser

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class VarParserTest {

    @Test
    fun `var with no initializer`() {
        val script = """
            var count;
        """.trimIndent()

        assertEquals(
            """
            Var(name=IDENTIFIER count null, initializer=null)
            """.trimIndent(),
            parse(script),
        )
    }
}
