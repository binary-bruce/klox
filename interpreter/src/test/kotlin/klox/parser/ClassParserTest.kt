package klox.parser

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ClassParserTest {
    @Test
    fun `empty class`() {
        val script = """
            class Duck {
            }
        """.trimIndent()

        assertEquals(
            """
            Class(name=IDENTIFIER Duck null, superClass=null, methods=[])
            """.trimIndent(),
            parse(script),
        )
    }

    @Test
    fun `empty class with inheritance`() {
        val script = """
            class Duck < Bird {
            }
        """.trimIndent()

        assertEquals(
            """
            Class(name=IDENTIFIER Duck null, superClass=Variable(name=IDENTIFIER Bird null), methods=[])
            """.trimIndent(),
            parse(script),
        )
    }

    @Test
    fun `class with one method`() {
        val script = """
            class Duck {
                fun fly() {
                }
                
                fun swim() {
                }
            }
        """.trimIndent()

        assertEquals(
            """
            Class(name=IDENTIFIER Duck null, superClass=null, methods=[Function(name=IDENTIFIER fly null, parameters=[], body=[]), Function(name=IDENTIFIER swim null, parameters=[], body=[])])
            """.trimIndent(),
            parse(script),
        )
    }
}
