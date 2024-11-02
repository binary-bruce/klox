package klox.interpreter

import klox.interpreter.InterpreterUtils.checkNumberOperands
import klox.interpreter.InterpreterUtils.isEqual
import klox.Lox
import klox.RuntimeError
import klox.ast.Expr
import klox.ast.Stmt
import klox.resolver.Environment
import klox.scanner.TokenType

class Interpreter : Expr.Visitor<Any>, Stmt.Visitor<Void> {
    private val locals = mutableMapOf<Expr, Int>()
    private val globals = Environment()
    private val environment: Environment = globals

    fun interpret(statements: List<Stmt>) {
        try {
            statements.forEach { execute(it) }
        } catch (e: RuntimeError) {
            Lox.runtimeError(e)
        }
    }

    fun resolve(expr: Expr, depth: Int) {
        locals[expr] = depth
    }

    private fun execute(statement: Stmt) {
        statement.accept(this)
    }

    private fun evaluate(expr: Expr): Any {
        return expr.accept(this)
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
                }
                if (left is String && right is String) {
                    left + right
                }
                throw RuntimeError(expr.operator, "Operands must be two numbers or two strings.")
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
        TODO("Not yet implemented")
    }

    override fun <R> visitGetExpr(expr: Expr.Get): R {
        TODO("Not yet implemented")
    }

    override fun <R> visitGroupingExpr(expr: Expr.Grouping): R {
        TODO("Not yet implemented")
    }

    override fun <R> visitLiteralExpr(expr: Expr.Literal): R {
        TODO("Not yet implemented")
    }

    override fun <R> visitLogicalExpr(expr: Expr.Logical): R {
        TODO("Not yet implemented")
    }

    override fun <R> visitSetExpr(expr: Expr.Set): R {
        TODO("Not yet implemented")
    }

    override fun <R> visitSuperExpr(expr: Expr.Super): R {
        TODO("Not yet implemented")
    }

    override fun <R> visitThisExpr(expr: Expr.This): R {
        TODO("Not yet implemented")
    }

    override fun <R> visitUnaryExpr(expr: Expr.Unary): R {
        TODO("Not yet implemented")
    }

    override fun <R> visitVariableExpr(expr: Expr.Variable): R {
        TODO("Not yet implemented")
    }

    override fun <R> visitBlockStmt(stmt: Stmt.Block): R {
        TODO("Not yet implemented")
    }

    override fun <R> visitClassStmt(stmt: Stmt.Class): R {
        TODO("Not yet implemented")
    }

    override fun <R> visitFunctionStmt(stmt: Stmt.Function): R {
        TODO("Not yet implemented")
    }

    override fun <R> visitExpressionStmt(stmt: Stmt.Expression): R {
        TODO("Not yet implemented")
    }

    override fun <R> visitIfStmt(stmt: Stmt.If): R {
        TODO("Not yet implemented")
    }

    override fun <R> visitPrintStmt(stmt: Stmt.Print): R {
        TODO("Not yet implemented")
    }

    override fun <R> visitReturnStmt(stmt: Stmt.Return): R {
        TODO("Not yet implemented")
    }

    override fun <R> visitVarStmt(stmt: Stmt.Var): R {
        TODO("Not yet implemented")
    }

    override fun <R> visitWhileStmt(stmt: Stmt.While): R {
        TODO("Not yet implemented")
    }

}
