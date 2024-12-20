package klox.interpreter

import klox.Lox
import klox.RuntimeError
import klox.ast.Expr
import klox.ast.Stmt
import klox.interpreter.InterpreterUtils.checkNumberOperand
import klox.interpreter.InterpreterUtils.checkNumberOperands
import klox.interpreter.InterpreterUtils.isEqual
import klox.interpreter.InterpreterUtils.isTruthy
import klox.interpreter.InterpreterUtils.stringify
import klox.resolver.Environment
import klox.resolver.Resolvable
import klox.scanner.Token
import klox.scanner.TokenType

class Interpreter : Expr.Visitor<Any>, Stmt.Visitor<Void>, Resolvable {
    private val locals = mutableMapOf<Expr, Int>()
    private val globals = Environment()
    private var environment: Environment = globals

    fun interpret(statements: List<Stmt>) {
        try {
            statements.forEach { execute(it) }
        } catch (e: RuntimeError) {
            Lox.runtimeError(e)
        }
    }

    override fun resolve(expr: Expr, depth: Int) {
        locals[expr] = depth
    }

    private fun execute(statement: Stmt) {
        statement.accept(this)
    }

    fun executeBlock(statements: List<Stmt>, environment: Environment) {
        val previous = this.environment
        try {
            this.environment = environment
            statements.forEach { execute(it) }
        } finally {
            this.environment = previous
        }
    }

    private fun evaluate(expr: Expr): Any {
        return expr.accept(this)
    }

    private fun lookUpVariable(name: Token, expr: Expr): Any? {
        val distance = locals[expr]
        return if (distance != null) {
            environment.getAt(distance, name.lexeme)
        } else {
            globals.get(name)
        }
    }

    override fun <R> visitAssignExpr(expr: Expr.Assign): R {
        val value = evaluate(expr.value)
        val distance = locals[expr]
        if (distance != null) {
            environment.assignAt(distance, expr.name, value)
        } else {
            globals.assign(expr.name, value)
        }

        return value as R
    }

    override fun <R> visitBinaryExpr(expr: Expr.Binary): R {
        val left = evaluate(expr.left)
        val right = evaluate(expr.right)
        return when (expr.operator.type) {
            TokenType.BANG_EQUAL -> !isEqual(left, right)
            TokenType.EQUAL_EQUAL -> isEqual(left, right)
            TokenType.GREATER -> {
                checkNumberOperands(expr.operator, left, right)
                left as Double > right as Double
            }

            TokenType.GREATER_EQUAL -> {
                checkNumberOperands(expr.operator, left, right)
                left as Double >= right as Double
            }

            TokenType.LESS -> {
                checkNumberOperands(expr.operator, left, right)
                (left as Double) < (right as Double)
            }

            TokenType.LESS_EQUAL -> {
                checkNumberOperands(expr.operator, left, right)
                left as Double <= right as Double
            }

            TokenType.MINUS -> {
                checkNumberOperands(expr.operator, left, right)
                left as Double - right as Double
            }

            TokenType.PLUS -> {
                if (left is Double && right is Double) {
                    left + right
                } else if (left is String && right is String) {
                    left + right
                } else {
                    throw RuntimeError(expr.operator, "Operands must be two numbers or two strings.")
                }
            }

            TokenType.SLASH -> {
                checkNumberOperands(expr.operator, left, right)
                left as Double / right as Double
            }

            TokenType.STAR -> {
                checkNumberOperands(expr.operator, left, right)
                left as Double * right as Double
            }

            else -> null
        } as R
    }

    override fun <R> visitCallExpr(expr: Expr.Call): R {
        val callee = evaluate(expr.callee)
        val arguments: List<Any> = expr.arguments.map { evaluate(it) }

        if (callee !is LoxCallable) {
            throw RuntimeError(expr.paren, "Can only call functions and classes.")
        }

        if (arguments.size != callee.arity()) {
            throw RuntimeError(expr.paren, "Expected ${callee.arity()} arguments but got ${arguments.size}.")
        }

        return callee.call(this, arguments) as R
    }

    override fun <R> visitGetExpr(expr: Expr.Get): R {
        val `object` = evaluate(expr.`object`)
        if (`object` is LoxInstance) {
            return `object`[expr.name] as R
        }

        throw RuntimeError(expr.name, "Only instances have properties.")
    }

    override fun <R> visitGroupingExpr(expr: Expr.Grouping): R {
        return evaluate(expr.expression) as R
    }

    override fun <R> visitLiteralExpr(expr: Expr.Literal): R {
        return expr.value as R
    }

    override fun <R> visitLogicalExpr(expr: Expr.Logical): R {
        val left = evaluate(expr.left)

        if (expr.operator.type === TokenType.OR) {
            if (isTruthy(left)) return left as R
        } else {
            if (!isTruthy(left)) return left as R
        }

        return evaluate(expr.right) as R
    }

    override fun <R> visitSetExpr(expr: Expr.Set): R {
        val `object` = evaluate(expr.`object`) as? LoxInstance
            ?: throw RuntimeError(expr.name, "Only instances have fields.")

        val value = evaluate(expr.value)
        `object`[expr.name] = value
        return value as R
    }

    override fun <R> visitSuperExpr(expr: Expr.Super): R {
        val distance = locals[expr]!!
        val superclass = environment.getAt(distance, "super") as LoxClass?

        // "this" is always one level nearer that "super"'s environment.

        // "this" is always one level nearer that "super"'s environment.
        val `object` = environment.getAt(distance - 1, "this") as LoxInstance?

        return superclass!!.findMethod(`object`!!, expr.method.lexeme) as R
            ?: throw RuntimeError(expr.method, "Undefined property '${expr.method.lexeme}'.")
    }

    override fun <R> visitThisExpr(expr: Expr.This): R {
        return lookUpVariable(expr.keyword, expr) as R
    }

    override fun <R> visitUnaryExpr(expr: Expr.Unary): R {
        val right = evaluate(expr.right)

        return when (expr.operator.type) {
            TokenType.BANG -> !isTruthy(right)
            TokenType.MINUS -> {
                checkNumberOperand(expr.operator, right)
                -(right as Double)
            }

            else -> null
        } as R
    }

    override fun <R> visitVariableExpr(expr: Expr.Variable): R {
        return lookUpVariable(expr.name, expr) as R
    }

    override fun <R> visitBlockStmt(stmt: Stmt.Block): R {
        executeBlock(stmt.statements, Environment(environment))
        return null as R
    }

    override fun <R> visitClassStmt(stmt: Stmt.Class): R {
        environment.define(stmt.name.lexeme, null)
        val superClass = if (stmt.superClass != null) {
            evaluate(stmt.superClass).also {
                if (it !is LoxClass) throw RuntimeError(stmt.name, "Superclass must be a class.")
                environment = Environment(environment)
                environment.define("super", it)
            }
        } else {
            null
        }

        val methods = mutableMapOf<String, LoxFunction>()
        stmt.methods.forEach {
            val function = LoxFunction(it, environment, it.name.lexeme == "init")
            methods[it.name.lexeme] = function
        }

        val klass = LoxClass(stmt.name.lexeme, superClass as LoxClass?, methods)
        if (superClass != null) {
            environment = environment.enclosing!!
        }
        environment.assign(stmt.name, klass)

        return null as R
    }

    override fun <R> visitFunctionStmt(stmt: Stmt.Function): R {
        val function = LoxFunction(stmt, environment, false)
        environment.define(stmt.name.lexeme, function)
        return null as R
    }

    override fun <R> visitExpressionStmt(stmt: Stmt.Expression): R {
        evaluate(stmt.expression)
        return null as R
    }

    override fun <R> visitIfStmt(stmt: Stmt.If): R {
        if (isTruthy(evaluate(stmt.condition))) {
            execute(stmt.thenBranch)
        } else if (stmt.elseBranch != null) {
            execute(stmt.elseBranch)
        }

        return null as R
    }

    override fun <R> visitPrintStmt(stmt: Stmt.Print): R {
        val value = evaluate(stmt.expression)
        println(stringify(value))

        return null as R
    }

    override fun <R> visitReturnStmt(stmt: Stmt.Return): R {
        val value = if (stmt.value != null) {
            evaluate(stmt.value)
        } else {
            null
        }

        throw Return(value)
    }

    override fun <R> visitVarStmt(stmt: Stmt.Var): R {
        val value = if (stmt.initializer != null) {
            evaluate(stmt.initializer)
        } else {
            null
        }

        environment.define(stmt.name.lexeme, value)

        return null as R
    }

    override fun <R> visitWhileStmt(stmt: Stmt.While): R {
        while (isTruthy(evaluate(stmt.condition))) {
            execute(stmt.body)
        }

        return null as R
    }

}
