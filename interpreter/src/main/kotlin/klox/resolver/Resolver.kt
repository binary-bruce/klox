package klox.resolver

import klox.Interpreter
import klox.ast.Expr

class Resolver(private val interpreter: Interpreter) : Expr.Visitor<Unit> {

    override fun <R> visitAssignExpr(expr: Expr.Assign): R {
        return Unit as R
    }

    override fun <R> visitBinaryExpr(expr: Expr.Binary): R {
        return Unit as R
    }

    override fun <R> visitCallExpr(expr: Expr.Call): R {
        return Unit as R
    }

    override fun <R> visitGetExpr(expr: Expr.Get): R {
        return Unit as R
    }

    override fun <R> visitGroupingExpr(expr: Expr.Grouping): R {
        return Unit as R
    }

    override fun <R> visitLiteralExpr(expr: Expr.Literal): R {
        return Unit as R
    }

    override fun <R> visitLogicalExpr(expr: Expr.Logical): R {
        return Unit as R
    }

    override fun <R> visitSetExpr(expr: Expr.Set): R {
        return Unit as R
    }

    override fun <R> visitSuperExpr(expr: Expr.Super): R {
        return Unit as R
    }

    override fun <R> visitThisExpr(expr: Expr.This): R {
        return Unit as R
    }

    override fun <R> visitUnaryExpr(expr: Expr.Unary): R {
        return Unit as R
    }

    override fun <R> visitVariableExpr(expr: Expr.Variable): R {
        return Unit as R
    }
}
