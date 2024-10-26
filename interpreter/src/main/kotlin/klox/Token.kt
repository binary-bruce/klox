package klox

data class Token(
    val type: TokenType,
    val lexme: String,
    val literal: Any?,
    val line: Int,
) {
    override fun toString(): String {
        return "$type $lexme $literal"
    }
}
