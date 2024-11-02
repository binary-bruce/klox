package klox.resolver

import klox.Interpreter
import klox.Lox
import klox.ast.Expr
import klox.ast.Stmt
import klox.scanner.Token
import java.lang.Boolean.FALSE
import java.util.*

class Resolver(private val interpreter: Interpreter) : Stmt.Visitor<Unit>, Expr.Visitor<Unit> {
    private val scopes = Stack<MutableMap<String, Boolean>>()

    private var currentClass = ClassType.NONE
    private var currentFunction = FunctionType.NONE

    private enum class ClassType {
        NONE,
        CLASS,
        SUBCLASS
    }

    private enum class FunctionType {
        NONE,
        FUNCTION,
        INITIALIZER,
        METHOD
    }

    fun resolve(statements: List<Stmt>) {
        statements.forEach { resolve(it) }
    }

    private fun resolve(stmt: Stmt) {
        stmt.accept(this)
    }

    private fun resolve(expr: Expr) {
        expr.accept(this)
    }

    private fun resolveLocal(expr: Expr, name: Token) {
        for (i in scopes.indices.reversed()) {
            if (scopes[i].containsKey(name.lexeme)) {
                interpreter.resolve(expr, scopes.size - 1 - i)
                return
            }
        }

        // Not found. Assume it is global.
    }

    private fun resolveFunction(function: Stmt.Function, type: FunctionType) {
        val enclosingFunction = currentFunction
        currentFunction = type
        beginScope()
        function.parameters.forEach { declare(it); define(it) }
        resolve(function.body)
        endScope()
        currentFunction = enclosingFunction
    }

    private fun beginScope() {
        scopes.push(HashMap())
    }

    private fun endScope() {
        scopes.pop()
    }

    private fun declare(name: Token) {
        if (scopes.isEmpty()) return
        if (scopes.peek().containsKey(name.lexeme)) {
            Lox.error(name, "Variable with this name already declared in this scope.")
        }
        scopes.peek()[name.lexeme] = false
    }

    private fun define(name: Token) {
        if (scopes.isEmpty()) return
        scopes.peek()[name.lexeme] = true
    }

    override fun <R> visitAssignExpr(expr: Expr.Assign): R {
        resolve(expr.value)
        resolveLocal(expr, expr.name)
        return Unit as R
    }

    override fun <R> visitBinaryExpr(expr: Expr.Binary): R {
        resolve(expr.left)
        resolve(expr.right)
        return Unit as R
    }

    override fun <R> visitCallExpr(expr: Expr.Call): R {
        resolve(expr.callee)
        expr.arguments.forEach { resolve(it) }
        return Unit as R
    }

    override fun <R> visitGetExpr(expr: Expr.Get): R {
        resolve(expr.`object`)
        return Unit as R
    }

    override fun <R> visitGroupingExpr(expr: Expr.Grouping): R {
        resolve(expr.expression)
        return Unit as R
    }

    override fun <R> visitLiteralExpr(expr: Expr.Literal): R {
        return Unit as R
    }

    override fun <R> visitLogicalExpr(expr: Expr.Logical): R {
        resolve(expr.left)
        resolve(expr.right)
        return Unit as R
    }

    override fun <R> visitSetExpr(expr: Expr.Set): R {
        resolve(expr.value)
        resolve(expr.`object`)
        return Unit as R
    }

    override fun <R> visitSuperExpr(expr: Expr.Super): R {
        if (currentClass == ClassType.NONE) {
            Lox.error(expr.keyword, "Cannot use 'super' outside of a class.")
        } else if (currentClass != ClassType.SUBCLASS) {
            Lox.error(expr.keyword, "Cannot use 'super' in a class with no superclass.")
        }

        resolveLocal(expr, expr.keyword)
        return Unit as R
    }

    override fun <R> visitThisExpr(expr: Expr.This): R {
        if (currentClass == ClassType.NONE) {
            Lox.error(expr.keyword, "Cannot use 'this' outside of a class.")
            return Unit as R
        }

        resolveLocal(expr, expr.keyword)
        return Unit as R
    }

    override fun <R> visitUnaryExpr(expr: Expr.Unary): R {
        resolve(expr.right)
        return Unit as R
    }

    override fun <R> visitVariableExpr(expr: Expr.Variable): R {
        if (!scopes.isEmpty() && scopes.peek()[expr.name.lexeme] === FALSE) {
            Lox.error(expr.name, "Cannot read local variable in its own initializer.")
        }

        resolveLocal(expr, expr.name)
        return Unit as R
    }

    override fun <R> visitBlockStmt(stmt: Stmt.Block): R {
        beginScope()
        resolve(stmt.statements)
        endScope()
        return Unit as R
    }

    override fun <R> visitClassStmt(stmt: Stmt.Class): R {
        declare(stmt.name)
        define(stmt.name)

        val enclosingClass = currentClass
        currentClass = ClassType.CLASS

        if (stmt.superClass != null) {
            currentClass = ClassType.SUBCLASS
            resolve(stmt.superClass)
            beginScope()
            scopes.peek()["super"] = true
        }

        beginScope()
        scopes.peek()["this"] = true

        stmt.methods.forEach {
            var declaration = FunctionType.METHOD
            if (it.name.lexeme == "init") {
                declaration = FunctionType.INITIALIZER
            }
            resolveFunction(it, declaration)
        }

        endScope()

        if (stmt.superClass != null) endScope()

        currentClass = enclosingClass
        return Unit as R
    }

    override fun <R> visitFunctionStmt(stmt: Stmt.Function): R {
        declare(stmt.name)
        define(stmt.name)

        resolveFunction(stmt, FunctionType.FUNCTION)
        return Unit as R
    }

    override fun <R> visitExpressionStmt(stmt: Stmt.Expression): R {
        resolve(stmt.expression)
        return Unit as R
    }

    override fun <R> visitIfStmt(stmt: Stmt.If): R {
        resolve(stmt.condition)
        resolve(stmt.thenBranch)
        if (stmt.elseBranch != null) resolve(stmt.elseBranch)
        return Unit as R
    }

    override fun <R> visitPrintStmt(stmt: Stmt.Print): R {
        resolve(stmt.expression)
        return Unit as R
    }

    override fun <R> visitReturnStmt(stmt: Stmt.Return): R {
        if (currentFunction == FunctionType.NONE) {
            Lox.error(stmt.keyword, "Cannot return from top-level code.")
        }
        if (stmt.value != null) {
            if (currentFunction == FunctionType.INITIALIZER) {
                Lox.error(stmt.keyword, "Cannot return a value from an initializer.")
            }
            resolve(stmt.value)
        }
        return Unit as R
    }

    override fun <R> visitVarStmt(stmt: Stmt.Var): R {
        declare(stmt.name)
        if (stmt.initializer != null) {
            resolve(stmt.initializer)
        }
        define(stmt.name)
        return Unit as R
    }

    override fun <R> visitWhileStmt(stmt: Stmt.While): R {
        resolve(stmt.condition)
        resolve(stmt.body)
        return Unit as R
    }
}
