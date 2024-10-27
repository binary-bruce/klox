package klox.parser

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class StatementParserTest {
    @Test
    fun `test for loop`() {
        assertEquals(
            "While(condition=Literal(value=true), body=Block(statements=[Expression(expression=Assign(name=IDENTIFIER a null, value=Unary(operator=BANG ! null, right=Variable(name=IDENTIFIER a null))))]))",
            parse("for(;;){ a = !a; }"),
        )

        assertEquals(
            "Block(statements=[Var(name=IDENTIFIER i null, initializer=Literal(value=0.0)), While(condition=Binary(left=Variable(name=IDENTIFIER i null), operator=LESS_EQUAL <= null, right=Literal(value=100.0)), body=Block(statements=[Block(statements=[Expression(expression=Assign(name=IDENTIFIER sum null, value=Binary(left=Variable(name=IDENTIFIER sum null), operator=PLUS + null, right=Variable(name=IDENTIFIER i null))))]), Expression(expression=Assign(name=IDENTIFIER i null, value=Binary(left=Variable(name=IDENTIFIER i null), operator=PLUS + null, right=Literal(value=1.0))))]))])",
            parse("for(var i = 0;i <= 100;i = i + 1){ sum = sum + i; }"),
        )
    }

    @Test
    fun `test if`() {
        assertEquals(
            "If(condition=Binary(left=Variable(name=IDENTIFIER a null), operator=EQUAL_EQUAL == null, right=Literal(value=19.0)), thenBranch=Expression(expression=Call(callee=Variable(name=IDENTIFIER greet null), paren=RIGHT_PAREN ) null, arguments=[])), elseBranch=null)",
            parse("if (a == 19) greet();")
        )

        assertEquals(
            "If(condition=Binary(left=Variable(name=IDENTIFIER a null), operator=EQUAL_EQUAL == null, right=Literal(value=0.0)), thenBranch=Expression(expression=Literal(value=true)), elseBranch=Block(statements=[Expression(expression=Literal(value=false))]))",
            parse("if (a == 0) true; else { false; }")
        )
    }
}
