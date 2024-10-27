package klox.parser

import klox.Lox
import klox.ast.Expr
import klox.ast.Stmt
import klox.scanner.Token
import klox.scanner.TokenType
import klox.scanner.TokenType.*

class Parser(private val tokens: List<Token>) {
    class ParseError : RuntimeException()

    private var current = 0

    public fun parse(): List<Stmt> {
        val statements = mutableListOf<Stmt>()
        while (!isAtEnd()) {
            statements.add(declaration())
        }

        return statements
    }

    private fun declaration(): Stmt {
        when {
            match(FUN) -> return function(FunctionKind.FUNCTION)
            match(CLASS) -> return classDeclaration()
            match(VAR) -> return varDeclaration()
        }

        throw NotImplementedError()
    }

    private fun varDeclaration(): Stmt {
        val name = consume(IDENTIFIER, "Expect variable name.")

        val initializer = if (match(EQUAL)) {
            expression()
        } else {
            null
        }
        consume(SEMICOLON, "Expect ';' after variable declaration.")

        return Stmt.Var(name, initializer)
    }

    private fun expression(): Expr {
        return assignment()
    }

    private fun assignment(): Expr {
        val expr = or()

        if (match(EQUAL)) {
            val equals = previous()
            val value = assignment()

            if (expr is Expr.Variable) {
                val name = expr.name
                return Expr.Assign(name, value)
            } else if (expr is Expr.Get) {
                return Expr.Set(expr.`object`, expr.name, value)
            }

            error(equals, "Invalid assignment target.")
        }

        return expr
    }

    private fun or(): Expr {
        var expr = and()

        while (match(OR)) {
            val operator = previous()
            val right = and()
            expr = Expr.Logical(expr, operator, right)
        }

        return expr
    }

    private fun and(): Expr {
        var expr = equality()

        while (match(AND)) {
            val operator = previous()
            val right = equality()
            expr = Expr.Logical(expr, operator, right)
        }

        return expr
    }

    private fun equality(): Expr {
        var expr = comparison()

        while (match(BANG_EQUAL) || match(EQUAL_EQUAL)) {
            val operator = previous()
            val right = comparison()
            expr = Expr.Binary(expr, operator, right)
        }

        return expr
    }

    private fun comparison(): Expr {
        var expr = addition()

        while (match(GREATER) || match(GREATER_EQUAL) || match(LESS) || match(LESS_EQUAL)) {
            val operator = previous()
            val right = addition()
            expr = Expr.Binary(expr, operator, right)
        }

        return expr
    }

    private fun addition(): Expr {
        var expr = multiplication()

        while (match(PLUS) || match(MINUS)) {
            val operator = previous()
            val right = multiplication()
            expr = Expr.Binary(expr, operator, right)
        }

        return expr
    }

    private fun multiplication(): Expr {
        var expr = unary()

        while (match(SLASH) || match(STAR)) {
            val operator = previous()
            val right = unary()
            expr = Expr.Binary(expr, operator, right)
        }

        return expr
    }

    private fun unary(): Expr {
        if (match(BANG) || match(MINUS)) {
            val operator = previous()
            val right = unary()
            return Expr.Unary(operator, right)
        }

        return call()
    }

    private fun call(): Expr {
        var expr = primary()

        while (true) {
            if (match(LEFT_PAREN)) {
                expr = finishCall(expr)
            } else if (match(DOT)) {
                val name = consume(IDENTIFIER, "Expect property name after '.'.")
                expr = Expr.Get(expr, name)
            } else {
                break
            }
        }

        return expr
    }

    private fun finishCall(callee: Expr): Expr {
        val arguments = mutableListOf<Expr>()
        if (!check(RIGHT_PAREN)) {
            do {
                if (arguments.size >= 8) {
                    error(peek(), "Cannot have more than 8 arguments.")
                }
                arguments.add(expression())
            } while (match(COMMA))
        }

        val paren = consume(RIGHT_PAREN, "Expect ')' after arguments.")

        return Expr.Call(callee, paren, arguments)
    }

    private fun primary(): Expr {
        return when {
            match(FALSE) -> Expr.Literal(false)
            match(TRUE) -> Expr.Literal(true)
            match(NIL) -> Expr.Literal(null)
            match(NUMBER) || match(STRING) -> Expr.Literal(previous().literal)
            match(SUPER) -> {
                val keyword = previous()
                consume(DOT, "Expect '.' after 'super'.")
                val method = consume(IDENTIFIER, "Expect super class method name.")

                Expr.Super(keyword, method)
            }

            match(THIS) -> Expr.This(previous())
            match(IDENTIFIER) -> Expr.Variable(previous())
            match(LEFT_PAREN) -> {
                val expr = expression()
                consume(RIGHT_PAREN, "Expect ')' after expression.")

                Expr.Grouping(expr)
            }

            else -> throw error(peek(), "Expect expression.")
        }
    }

    private fun classDeclaration(): Stmt {
        val name = consume(IDENTIFIER, "Expect class name.")

        val superClass = if (match(LESS)) {
            consume(IDENTIFIER, "Expect super class name.")
            Expr.Variable(previous())
        } else {
            null
        }

        consume(LEFT_BRACE, "Expect '{' before class body.")

        val methods = mutableListOf<Stmt.Function>()
        while (!check(RIGHT_BRACE) && !isAtEnd()) {
            consume(FUN, "Expect keyword `fun`.")
            methods.add(function(FunctionKind.METHOD))
        }

        consume(RIGHT_BRACE, "Expect '}' after class body.")

        return Stmt.Class(name, superClass, methods)
    }

    private fun function(kind: FunctionKind): Stmt.Function {
        val name = consume(IDENTIFIER, "Expect $kind name.")
        consume(LEFT_PAREN, "Expect '(' after $kind name.")
        val parameters = mutableListOf<Token>()
        if (!check(RIGHT_PAREN)) {
            do {
                if (parameters.size >= 8) {
                    error(peek(), "Cannot have more than 8 parameters")
                }

                parameters.add(consume(IDENTIFIER, "Expect parameter name."))
            } while (match(COMMA))
        }

        consume(RIGHT_PAREN, "Expect ')' after parameters.")
        consume(LEFT_BRACE, "Expect '{' before $kind body.")

        val body = block()
        return Stmt.Function(name, parameters, body)
    }

    private fun block(): List<Stmt> {
        val statements = mutableListOf<Stmt>()
        while (!check(RIGHT_BRACE) && !isAtEnd()) {
            statements.add(declaration())
        }

        consume(RIGHT_BRACE, "Expect '}' after the block.")

        return statements
    }

    private fun error(token: Token, message: String): ParseError {
        Lox.error(token, message)
        return ParseError()
    }

    private fun match(type: TokenType): Boolean {
        return if (check(type)) {
            advance()
            true
        } else {
            false
        }
    }

    private fun consume(type: TokenType, message: String): Token {
        if (check(type)) return advance()

        throw error(peek(), message)
    }

    private fun advance(): Token {
        if (!isAtEnd()) current++

        return previous()
    }

    private fun check(type: TokenType): Boolean = if (!isAtEnd()) peek().type == type else false

    private fun isAtEnd(): Boolean = peek().type == EOF

    private fun previous(): Token = tokens[current - 1]

    private fun peek(): Token = tokens[current]
}
