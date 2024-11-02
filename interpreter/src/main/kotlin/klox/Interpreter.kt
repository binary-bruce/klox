package klox

import klox.ast.Expr
import klox.ast.Stmt

class Interpreter : Expr.Visitor<Any>, Stmt.Visitor<Void> {
    fun interpret(statements: List<Stmt>) {
        try {
            statements.forEach { execute(it) }
        } catch (e: RuntimeError) {
            Lox.runtimeError(e)
        }
    }

    private fun execute(statement: Stmt) {
        statement.accept(this)
    }

    override fun <R> visitAssignExpr(expr: Expr.Assign): R {
        TODO("Not yet implemented")
    }

    override fun <R> visitBinaryExpr(expr: Expr.Binary): R {
        TODO("Not yet implemented")
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
