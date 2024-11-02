package klox.interpreter

import klox.Return
import klox.ast.Stmt
import klox.resolver.Environment


internal class LoxFunction(declaration: Stmt.Function, closure: Environment, private val isInitializer: Boolean) : LoxCallable {
    private val declaration: Stmt.Function
    private val closure: Environment

    init {
        this.closure = closure
        this.declaration = declaration
    }

    fun bind(instance: LoxInstance): LoxFunction {
        val environment = Environment(closure)
        environment.define("this", instance)
        return LoxFunction(declaration, environment, isInitializer)
    }

    override fun toString(): String {
        return "<fn " + declaration.name.lexeme + ">"
    }

    override fun arity(): Int {
        return declaration.parameters.size
    }

    override fun call(interpreter: Interpreter, arguments: List<Any>): Any {
        val environment = Environment(closure)
        for (i in 0 until declaration.parameters.size) {
            environment.define(declaration.parameters[i].lexeme, arguments[i])
        }
        try {
            interpreter.executeBlock(declaration.body, environment)
        } catch (returnValue: Return) {
            return returnValue.value
        }
        return (if (isInitializer) closure.getAt(0, "this") else null) as Any
    }
}

