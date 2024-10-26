package klox.scanner

object Keywords {
    val value = mutableMapOf<String, TokenType>().apply {
        put("and", TokenType.AND)
        put("class", TokenType.CLASS)
        put("else", TokenType.ELSE)
        put("false", TokenType.FALSE)
        put("for", TokenType.FOR)
        put("fun", TokenType.FUN)
        put("if", TokenType.IF)
        put("nil", TokenType.NIL)
        put("or", TokenType.OR)
        put("print", TokenType.PRINT)
        put("return", TokenType.RETURN)
        put("super", TokenType.SUPER)
        put("this", TokenType.THIS)
        put("true", TokenType.TRUE)
        put("var", TokenType.VAR)
        put("while", TokenType.WHILE)
    }.toMap()
}
