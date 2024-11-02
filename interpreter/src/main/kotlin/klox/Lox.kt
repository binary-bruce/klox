package klox

import klox.ast.Stmt
import klox.interpreter.Interpreter
import klox.parser.Parser
import klox.resolver.Resolver
import klox.scanner.Scanner
import klox.scanner.Token
import klox.scanner.TokenType

object Lox {
    private var hadError = false
    private var hadRuntimeError = false
    private val interpreter = Interpreter()

    fun run(source: String) {
        val statements = source.toStatements()
        val resolver = Resolver(interpreter)
        resolver.resolve(statements)

        if (hadError) return

        interpreter.interpret(statements)
    }

    private fun String.toStatements(): List<Stmt> {
        val tokens = Scanner(this).scanTokens()
        return Parser(tokens).parse()
    }

    fun runtimeError(error: RuntimeError) {
        System.err.println("${error.message}\n[line ${error.token.line}]")
        hadRuntimeError = true
    }

    fun error(line: Int, message: String) {
        report(line, "", message)
    }

    fun error(token: Token, message: String) {
        if (token.type == TokenType.EOF) {
            report(token.line, " at end", message)
        } else {
            report(token.line, " at '${token.lexeme}'", message)
        }
    }

    private fun report(line: Int, where: String, message: String) {
        System.err.println("[line $line] Error $where: $message")
        hadError = true
    }
}
