package klox.scanner

import kotlin.system.exitProcess

class Scanner(
    private val source: String,
) {

    private val tokens: MutableList<Token> = mutableListOf()
    private var start = 0
    private var current = 0
    private var line = 1

    private val keywords = Keywords.value

    public fun scanTokens(): List<Token> {
        if (tokens.isEmpty()) {
            while (!isAtEnd()) {
                start = current
                scanToken()
            }

            tokens.add(Token(TokenType.EOF, "", null, line))
        }

        return tokens
    }

    private fun scanToken() {
        when (val c = advance()) {
            '(' -> addToken(TokenType.LEFT_PAREN)
            ')' -> addToken(TokenType.RIGHT_PAREN)
            '{' -> addToken(TokenType.LEFT_BRACE)
            '}' -> addToken(TokenType.RIGHT_BRACE)
            ',' -> addToken(TokenType.COMMA)
            '.' -> addToken(TokenType.DOT)
            '-' -> addToken(TokenType.MINUS)
            '+' -> addToken(TokenType.PLUS)
            ';' -> addToken(TokenType.SEMICOLON)
            '*' -> addToken(TokenType.STAR)
            '!' -> addToken(if (match('=')) TokenType.BANG_EQUAL else TokenType.BANG)
            '=' -> addToken(if (match('=')) TokenType.EQUAL_EQUAL else TokenType.EQUAL)
            '<' -> addToken(if (match('=')) TokenType.LESS_EQUAL else TokenType.LESS)
            '>' -> addToken(if (match('=')) TokenType.GREATER_EQUAL else TokenType.GREATER)
            '/' -> if (match('/')) {
                // A comment goes until the end of the line.
                while (peek() != '\n' && !isAtEnd()) advance()
            } else {
                addToken(TokenType.SLASH)
            }

            ' ', '\r', '\t' -> {}
            '\n' -> line++
            '"' -> string()
            else -> if (c.isDigit()) {
                number()
            } else if (c.isAlpha()) {
                identifier()
            } else {
                exitProcess(0)
            }
        }
    }

    private fun string() {
        while (peek() != '\"' && !isAtEnd()) {
            if (peek() == '\n') line++
            advance()
        }

        if (isAtEnd()) {
            exitProcess(0)
        }

        advance() // bypass closing `"`

        val value = source.substring(start + 1, current - 1)
        addToken(TokenType.STRING, value)
    }

    private fun number() {
        while (peek().isDigit()) advance()

        if (peek() == '.' && peekNext().isDigit()) {
            advance()
            while (peek().isDigit()) advance()
        }

        addToken(TokenType.NUMBER, source.substring(start, current).toDouble())
    }

    private fun identifier() {
        while (peek().isAlphaNumeric()) advance()

        val text = source.substring(start, current)
        addToken(keywords[text] ?: TokenType.IDENTIFIER)
    }

    private fun match(expected: Char): Boolean {
        return when {
            isAtEnd() -> false
            source[current] != expected -> false
            else -> {
                current++
                true
            }
        }
    }

    private fun isAtEnd(): Boolean {
        return current >= source.length
    }

    private fun advance(): Char {
        current++
        return source[current - 1]
    }

    private fun peek(): Char {
        return if (isAtEnd()) Char(0) else source[current]
    }

    private fun peekNext(): Char {
        return if (current + 1 >= source.length) Char(0) else source[current]
    }

    private fun Char.isAlpha(): Boolean = isLetter() || equals('_')

    private fun Char.isAlphaNumeric(): Boolean = isAlpha() || isDigit()

    private fun addToken(tokenType: TokenType, literal: Any? = null) {
        val text = source.substring(start, current)
        tokens.add(Token(tokenType, text, literal, line))
    }
}
