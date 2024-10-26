package klox.parser

import klox.ast.Stmt
import klox.scanner.Scanner

fun parse(script: String): String {
    return parseAst(script).joinToString(System.lineSeparator())
}

fun parseAst(script: String): List<Stmt> {
    val tokens = Scanner(script).scanTokens().also { println(it) }
    val parser = Parser(tokens)
    return parser.parse()
}
