package klox.resolver

import klox.Interpreter
import klox.ast.Expr
import klox.ast.Stmt

class Resolver(private val interpreter: Interpreter) : Expr.Visitor<Void>, Stmt.Visitor<Void> {

    fun resolve(statements: List<Stmt>) {
        statements.forEach { resolve(it) }
    }

    private fun resolve(statement: Stmt) {
        statement.accept(this)
    }

    override fun <R> visit(expr: Expr.Assign): R {
        TODO("Not yet implemented")
    }

    override fun <R> visit(expr: Expr.Binary): R {
        TODO("Not yet implemented")
    }

    override fun <R> visit(expr: Expr.Call): R {
        TODO("Not yet implemented")
    }

    override fun <R> visit(expr: Expr.Get): R {
        TODO("Not yet implemented")
    }

    override fun <R> visit(expr: Expr.Grouping): R {
        TODO("Not yet implemented")
    }

    override fun <R> visit(expr: Expr.Literal): R {
        TODO("Not yet implemented")
    }

    override fun <R> visit(expr: Expr.Logical): R {
        TODO("Not yet implemented")
    }

    override fun <R> visit(expr: Expr.Set): R {
        TODO("Not yet implemented")
    }

    override fun <R> visit(expr: Expr.Super): R {
        TODO("Not yet implemented")
    }

    override fun <R> visit(expr: Expr.This): R {
        TODO("Not yet implemented")
    }

    override fun <R> visit(expr: Expr.Unary): R {
        TODO("Not yet implemented")
    }

    override fun <R> visit(expr: Expr.Variable): R {
        TODO("Not yet implemented")
    }

    override fun <R> visit(expr: Stmt.Block): R {
        TODO("Not yet implemented")
    }

    override fun <R> visit(expr: Stmt.Class): R {
        TODO("Not yet implemented")
    }

    override fun <R> visit(expr: Stmt.Function): R {
        TODO("Not yet implemented")
    }

    override fun <R> visit(expr: Stmt.Expression): R {
        TODO("Not yet implemented")
    }

    override fun <R> visit(expr: Stmt.If): R {
        TODO("Not yet implemented")
    }

    override fun <R> visit(expr: Stmt.Print): R {
        TODO("Not yet implemented")
    }

    override fun <R> visit(expr: Stmt.Return): R {
        TODO("Not yet implemented")
    }

    override fun <R> visit(expr: Stmt.Var): R {
        TODO("Not yet implemented")
    }

    override fun <R> visit(expr: Stmt.While): R {
        TODO("Not yet implemented")
    }

}
