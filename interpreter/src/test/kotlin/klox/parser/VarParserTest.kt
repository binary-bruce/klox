package klox.parser

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class VarParserTest {

    @Test
    fun `var with no initializer`() {
        assertEquals(
            "Var(name=IDENTIFIER count null, initializer=null)",
            parse("var count;"),
        )
    }

    @Test
    fun `test primary`() {
        assertEquals(
            "Var(name=IDENTIFIER a null, initializer=Variable(name=IDENTIFIER b null))",
            parse("var a = b;"),
        )

        assertEquals(
            "Var(name=IDENTIFIER a null, initializer=Literal(value=12.0))",
            parse("var a = 12;"),
        )

        assertEquals(
            "Var(name=IDENTIFIER a null, initializer=Literal(value=hello))",
            parse("var a = \"hello\";"),
        )

        assertEquals(
            "Var(name=IDENTIFIER a null, initializer=Literal(value=false))",
            parse("var a = false;"),
        )

        assertEquals(
            "Var(name=IDENTIFIER a null, initializer=Literal(value=null))",
            parse("var a = nil;"),
        )

        assertEquals(
            "Var(name=IDENTIFIER a null, initializer=Call(callee=Super(keyword=SUPER super null, method=IDENTIFIER greet null), paren=RIGHT_PAREN ) null, arguments=[]))",
            parse("var a = super.greet();"),
        )

        assertEquals(
            "Var(name=IDENTIFIER a null, initializer=This(keyword=THIS this null))",
            parse("var a = this;"),
        )

        assertEquals(
            "Var(name=IDENTIFIER a null, initializer=Grouping(expression=Literal(value=10.0)))",
            parse("var a = (10);"),
        )
    }

    @Test
    fun `test function call`() {
        assertEquals(
            "Var(name=IDENTIFIER a null, initializer=Call(callee=Variable(name=IDENTIFIER add null), paren=RIGHT_PAREN ) null, arguments=[Literal(value=1.0), Literal(value=2.0)]))",
            parse("var a = add(1, 2);"),
        )

        assertEquals(
            "Var(name=IDENTIFIER a null, initializer=Call(callee=Variable(name=IDENTIFIER sum null), paren=RIGHT_PAREN ) null, arguments=[]))",
            parse("var a = sum();"),
        )

        assertEquals(
            "Var(name=IDENTIFIER a null, initializer=Call(callee=Get(object=Variable(name=IDENTIFIER color null), name=IDENTIFIER getCode null), paren=RIGHT_PAREN ) null, arguments=[]))",
            parse("var a = color.getCode();"),
        )
    }

    @Test
    fun `test unary`() {
        assertEquals(
            "Var(name=IDENTIFIER a null, initializer=Unary(operator=MINUS - null, right=Variable(name=IDENTIFIER b null)))",
            parse("var a = -b;"),
        )

        assertEquals(
            "Var(name=IDENTIFIER a null, initializer=Unary(operator=BANG ! null, right=Literal(value=10.0)))",
            parse("var a = !10;"),
        )
    }

    @Test
    fun `test multiplication`() {
        assertEquals(
            "Var(name=IDENTIFIER a null, initializer=Binary(left=Literal(value=2.0), operator=STAR * null, right=Literal(value=8.0)))",
            parse("var a = 2 * 8;"),
        )

        assertEquals(
            "Var(name=IDENTIFIER a null, initializer=Binary(left=Literal(value=2.0), operator=SLASH / null, right=Literal(value=8.0)))",
            parse("var a = 2 / 8;"),
        )
    }

    @Test
    fun `test addition`() {
        assertEquals(
            "Var(name=IDENTIFIER a null, initializer=Binary(left=Binary(left=Literal(value=1.0), operator=PLUS + null, right=Binary(left=Literal(value=2.0), operator=STAR * null, right=Literal(value=3.0))), operator=MINUS - null, right=Literal(value=4.0)))",
            parse("var a = 1 + 2 * 3 - 4;"),
        )
    }

    @Test
    fun `test comparison`() {
        assertEquals(
            "Var(name=IDENTIFIER a null, initializer=Binary(left=Literal(value=1.0), operator=GREATER > null, right=Literal(value=2.0)))",
            parse("var a = 1 > 2;"),
        )

        assertEquals(
            "Var(name=IDENTIFIER a null, initializer=Binary(left=Literal(value=1.0), operator=LESS_EQUAL <= null, right=Variable(name=IDENTIFIER b null)))",
            parse("var a = 1 <= b;"),
        )
    }

    @Test
    fun `test and or`() {
        assertEquals(
            "Var(name=IDENTIFIER a null, initializer=Logical(left=Variable(name=IDENTIFIER b null), operator=AND and null, right=Variable(name=IDENTIFIER c null)))",
            parse("var a = b and c;"),
        )

        assertEquals(
            "Var(name=IDENTIFIER a null, initializer=Logical(left=Variable(name=IDENTIFIER b null), operator=OR or null, right=Literal(value=false)))",
            parse("var a = b or false;"),
        )
    }
}
