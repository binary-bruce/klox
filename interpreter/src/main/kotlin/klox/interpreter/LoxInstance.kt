package klox.interpreter

import klox.RuntimeError
import klox.scanner.Token


internal class LoxInstance(klass: LoxClass) {
    private val klass: LoxClass
    private val fields: MutableMap<String, Any> = HashMap()

    init {
        this.klass = klass
    }

    operator fun get(name: Token): Any? {
        if (fields.containsKey(name.lexeme)) {
            return fields[name.lexeme]
        }
        val method: LoxFunction? = klass.findMethod(this, name.lexeme)
        if (method != null) return method
        throw RuntimeError(name, "Undefined property '" + name.lexeme + "'.")
    }

    operator fun set(name: Token, value: Any) {
        fields[name.lexeme] = value
    }

    override fun toString(): String {
        return klass.name + " instance"
    }
}

