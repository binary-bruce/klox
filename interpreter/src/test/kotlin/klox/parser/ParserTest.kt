package klox.parser

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

class ParserTest {

    @Test
    fun testParse() {
        val script = """
            for (var i = 1; i < 5; i = i + 1) {
              print i * i;
            }

            class Duck {
              init(name) {
                this.name = name;
              }

              quack() {
                print this.name + " quacks";
              }
            }

            var duck = Duck("Waddles");
            duck.quack();

            fun make_adder(n) {
              fun adder(i) {
                return n + i;
              }
              return adder;
            }
            var add5 = make_adder(5);
            print add5(1);
            print add5(100);
        """.trimIndent()

        assertDoesNotThrow {
            parse(script)
        }
    }
}
