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
        throw NotImplementedError()
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
