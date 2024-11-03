package klox.resolver

import klox.Lox
import klox.ast.Expr
import klox.parser.parseAst
import org.junit.jupiter.api.Test

class ResolverTest {
    class DepthResolverable : Resolvable {
        private val locals = mutableMapOf<Expr, Int>()

        override fun resolve(expr: Expr, depth: Int) {
            // println("RESOLVING $expr")
            locals[expr] = depth
        }

        override fun toString(): String {
            return locals.map { "${it.key.hashCode()} ${it.key} -> ${it.value}" }.joinToString(System.lineSeparator())
        }
    }

    @Test
    fun testDepth() {
        val script = """
            for(var i = 0; i < 5; i = i + 1){ print i; }
            var aa = 1;
            fun a() {
                var locala = 10;
                fun b() {
                    var localb = 42;
                    localb = localb + 1;
                    return aa;
                }
                
                aa = aa + 1;
                return b;
            }
            var b = a()();
            print b;
        """.trimIndent()

        val ast = parseAst(script).also { println(it) }
        val resolvable = DepthResolverable()

        Resolver(resolvable).resolve(ast)
        println(resolvable)
    }
}
