package klox.resolver

import klox.RuntimeError
import klox.scanner.Token

data class Environment(val enclosing: Environment? = null) {
    private val values = mutableMapOf<String, Any>()

    fun get(name: Token): Any {
        if (values.containsKey(name.lexme)) {
            return values[name.lexme]!!
        }

        if (enclosing != null) return enclosing.get(name)

        throw RuntimeError(name, "Undefined variable '${name.lexme}'.")
    }

    fun assign(name: Token, value: Any) {
        if (values.containsKey(name.lexme)) {
            values[name.lexme] = value
            return;
        }

        if (enclosing != null) {
            enclosing.assign(name, value)
            return
        }

        throw RuntimeError(name, "Undefined variable '${name.lexme}'.")
    }

    fun define(name: String, value: Any) {
        values[name] = value
    }

    fun ancestor(distance: Int): Environment {
        var environment: Environment = this
        for (i in 0 until distance) {
            environment = environment.enclosing!!
        }

        return environment
    }

    fun getAt(distance: Int, name: String): Any? {
        return ancestor(distance).values[name]
    }

    fun assignAt(distance: Int, name: Token, value: Any) {
        ancestor(distance).values[name.lexme] = value
    }
}
