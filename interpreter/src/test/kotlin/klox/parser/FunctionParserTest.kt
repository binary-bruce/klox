package klox.parser

import klox.scanner.Scanner
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class FunctionParserTest {
    @Test
    fun `function has no parameter`() {
        val script = """
            fun f1() {
            }
        """.trimIndent()

        assertEquals(
            """
            Function(name=IDENTIFIER f1 null, parameters=[], body=[])
            """.trimIndent(),
            parse(script),
        )
    }

    @Test
    fun `function has parameters`() {
        val script = """
            fun f1(a, b, c) {
            }
        """.trimIndent()

        assertEquals(
            """
            Function(name=IDENTIFIER f1 null, parameters=[IDENTIFIER a null, IDENTIFIER b null, IDENTIFIER c null], body=[])
            """.trimIndent(),
            parse(script),
        )
    }

    @Test
    fun `function has inner function`() {
        val script = """
            fun f1() {
                fun f2() {
                }
            }
        """.trimIndent()

        assertEquals(
            """
            Function(name=IDENTIFIER f1 null, parameters=[], body=[Function(name=IDENTIFIER f2 null, parameters=[], body=[])])
            """.trimIndent(),
            parse(script),
        )
    }

    private fun parse(script: String): String {
        val tokens = Scanner(script).scanTokens().also { println(it) }
        val parser = Parser(tokens)
        return parser.parse().joinToString(System.lineSeparator())
    }
}
