package klox.interpreter


internal class LoxClass(
    private val name: String,
    private val superClass: LoxClass?,
    private val methods: Map<String, LoxFunction>,
) : LoxCallable {

    fun findMethod(instance: LoxInstance, name: String): LoxFunction? {
        return if (methods.containsKey(name)) {
            methods[name]!!.bind(instance)
        } else {
            superClass?.findMethod(instance, name)
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

