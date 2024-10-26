package klox

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ScannerTest {

    @Test
    fun forLoop() {
        check(
            """
            for (var i = 1; i < 5; i = i + 1) {
                print i * i;
            }
            """.trimIndent(),

            """
            FOR for null
            LEFT_PAREN ( null
            VAR var null
            IDENTIFIER i null
            EQUAL = null
            NUMBER 1 1.0
            SEMICOLON ; null
            IDENTIFIER i null
            LESS < null
            NUMBER 5 5.0
            SEMICOLON ; null
            IDENTIFIER i null
            EQUAL = null
            IDENTIFIER i null
            PLUS + null
            NUMBER 1 1.0
            RIGHT_PAREN ) null
            LEFT_BRACE { null
            PRINT print null
            IDENTIFIER i null
            STAR * null
            IDENTIFIER i null
            SEMICOLON ; null
            RIGHT_BRACE } null
            EOF  null
            """.trimIndent(),
        )
    }

    @Test
    fun classDefinition() {
        check(
            """
            class Duck {
              init(name) {
                this.name = name;
              }
    
              quack() {
                print this.name + " quacks";
              }
            }
            """.trimIndent(),

            """
            CLASS class null
            IDENTIFIER Duck null
            LEFT_BRACE { null
            IDENTIFIER init null
            LEFT_PAREN ( null
            IDENTIFIER name null
            RIGHT_PAREN ) null
            LEFT_BRACE { null
            THIS this null
            DOT . null
            IDENTIFIER name null
            EQUAL = null
            IDENTIFIER name null
            SEMICOLON ; null
            RIGHT_BRACE } null
            IDENTIFIER quack null
            LEFT_PAREN ( null
            RIGHT_PAREN ) null
            LEFT_BRACE { null
            PRINT print null
            THIS this null
            DOT . null
            IDENTIFIER name null
            PLUS + null
            STRING " quacks"  quacks
            SEMICOLON ; null
            RIGHT_BRACE } null
            RIGHT_BRACE } null
            EOF  null
            """.trimIndent()
        )
    }

    @Test
    fun callObject() {
        check(
            """
            var duck = Duck("Waddles");
            duck.quack();
            """.trimIndent(),

            """
            VAR var null
            IDENTIFIER duck null
            EQUAL = null
            IDENTIFIER Duck null
            LEFT_PAREN ( null
            STRING "Waddles" Waddles
            RIGHT_PAREN ) null
            SEMICOLON ; null
            IDENTIFIER duck null
            DOT . null
            IDENTIFIER quack null
            LEFT_PAREN ( null
            RIGHT_PAREN ) null
            SEMICOLON ; null
            EOF  null
            """.trimIndent()
        )
    }

    @Test
    fun closure() {
        check(
            """
            fun make_adder(n) {
              fun adder(i) {
                return n + i;
              }
              return adder;
            }
            var add5 = make_adder(5);
            """.trimIndent(),

            """
            FUN fun null
            IDENTIFIER make_adder null
            LEFT_PAREN ( null
            IDENTIFIER n null
            RIGHT_PAREN ) null
            LEFT_BRACE { null
            FUN fun null
            IDENTIFIER adder null
            LEFT_PAREN ( null
            IDENTIFIER i null
            RIGHT_PAREN ) null
            LEFT_BRACE { null
            RETURN return null
            IDENTIFIER n null
            PLUS + null
            IDENTIFIER i null
            SEMICOLON ; null
            RIGHT_BRACE } null
            RETURN return null
            IDENTIFIER adder null
            SEMICOLON ; null
            RIGHT_BRACE } null
            VAR var null
            IDENTIFIER add5 null
            EQUAL = null
            IDENTIFIER make_adder null
            LEFT_PAREN ( null
            NUMBER 5 5.0
            RIGHT_PAREN ) null
            SEMICOLON ; null
            EOF  null
            """.trimIndent()
        )
    }

    @Test
    fun print() {
        check(
            """
            print add5(1);
            print add5(100);
            """.trimIndent(),

            """
            PRINT print null
            IDENTIFIER add5 null
            LEFT_PAREN ( null
            NUMBER 1 1.0
            RIGHT_PAREN ) null
            SEMICOLON ; null
            PRINT print null
            IDENTIFIER add5 null
            LEFT_PAREN ( null
            NUMBER 100 100.0
            RIGHT_PAREN ) null
            SEMICOLON ; null
            EOF  null
            """.trimIndent()
        )
    }

    private fun check(script: String, expectedTokens: String) {
        assertEquals(expectedTokens, getAndFormatTokens(script))
    }

    private fun getAndFormatTokens(script: String): String {
        return Scanner(script).scanTokens().joinToString(System.lineSeparator())
    }
}
