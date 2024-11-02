package klox.resolver

import klox.Interpreter
import klox.Lox
import klox.ast.Expr
import klox.scanner.Token
import java.lang.Boolean.FALSE
import java.util.*

class Resolver(private val interpreter: Interpreter) : Expr.Visitor<Unit> {
    private val scopes = Stack<MutableMap<String, Boolean>>()

    private var currentClass: ClassType = ClassType.NONE

    private enum class ClassType {
        NONE,
        CLASS,
        SUBCLASS
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
}
