package klox

import org.junit.jupiter.api.Test

class LoxTest {
    @Test
    fun execute() {
        Lox.run(
            """
                fun add(a, b) {
                  return a + b;
                }
                print add(1, 3);
            """.trimIndent()
        )

        Lox.run(
            """
                class Duck {
                    init(name) {
                        this.name = name;
                    }
    
                    quack() {
                        print this.name + " quacks";
                    }
                }
                
                var duck = Duck("duck");
                duck.quack();
            """.trimIndent()
        )
    }
}
