package klox.interpreter


internal class LoxClass(val name: String, val superclass: LoxClass?, methods: Map<String, LoxFunction>) : LoxCallable {
    private val methods: Map<String, LoxFunction>

    init {
        this.methods = methods
    }

    fun findMethod(instance: LoxInstance, name: String): LoxFunction? {
        return if (methods.containsKey(name)) {
            methods[name]!!.bind(instance)
        } else {
            superclass?.findMethod(instance, name)
        }
    }

    override fun toString(): String {
        return name
    }

    override fun call(interpreter: Interpreter, arguments: List<Any>): Any {
        val instance = LoxInstance(this)
        val initializer: LoxFunction? = methods["init"]
        initializer?.bind(instance)?.call(interpreter, arguments)
        return instance
    }

    override fun arity(): Int {
        val initializer: LoxFunction = methods["init"] ?: return 0
        return initializer.arity()
    }
}

