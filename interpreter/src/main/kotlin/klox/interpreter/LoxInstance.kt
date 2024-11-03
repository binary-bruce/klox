package klox.interpreter

import klox.RuntimeError
import klox.scanner.Token

internal class LoxInstance(private val klass: LoxClass) {
    private val fields: MutableMap<String, Any> = HashMap()

    operator fun get(name: Token): Any? {
        if (fields.containsKey(name.lexeme)) {
            return fields[name.lexeme]
        }

        return klass.findMethod(this, name.lexeme)
            ?: throw RuntimeError(name, "Undefined property '${name.lexeme}'.")
    }

    operator fun set(name: Token, value: Any) {
        fields[name.lexeme] = value
    }

    override fun toString(): String {
        return "$klass instance"
    }
}

