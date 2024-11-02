package klox.ast

import klox.scanner.Token

sealed class Expr {
    interface Visitor<R> {
        fun <R> visitAssignExpr(expr: Assign): R
        fun <R> visitBinaryExpr(expr: Binary): R
        fun <R> visitCallExpr(expr: Call): R
        fun <R> visitGetExpr(expr: Get): R
        fun <R> visitGroupingExpr(expr: Grouping): R
        fun <R> visitLiteralExpr(expr: Literal): R
        fun <R> visitLogicalExpr(expr: Logical): R
        fun <R> visitSetExpr(expr: Set): R
        fun <R> visitSuperExpr(expr: Super): R
        fun <R> visitThisExpr(expr: This): R
        fun <R> visitUnaryExpr(expr: Unary): R
        fun <R> visitVariableExpr(expr: Variable): R
    }

    abstract fun <R> accept(visitor: Visitor<R>): R

    data class Assign(val name: Token, val value: Expr) : Expr() {
        override fun <R> accept(visitor: Visitor<R>): R = visitor.visitAssignExpr(this)
    }

    data class Binary(val left: Expr, val operator: Token, val right: Expr) : Expr() {
        override fun <R> accept(visitor: Visitor<R>): R = visitor.visitBinaryExpr(this)
    }

    data class Call(val callee: Expr, val paren: Token, val arguments: List<Expr>) : Expr() {
        override fun <R> accept(visitor: Visitor<R>): R = visitor.visitCallExpr(this)
    }

    data class Get(val `object`: Expr, val name: Token) : Expr() {
        override fun <R> accept(visitor: Visitor<R>): R = visitor.visitGetExpr(this)
    }

    data class Grouping(val expression: Expr) : Expr() {
        override fun <R> accept(visitor: Visitor<R>): R = visitor.visitGroupingExpr(this)
    }

    data class Literal(val value: Any?) : Expr() {
        override fun <R> accept(visitor: Visitor<R>): R = visitor.visitLiteralExpr(this)
    }

    data class Logical(val left: Expr, val operator: Token, val right: Expr) : Expr() {
        override fun <R> accept(visitor: Visitor<R>): R = visitor.visitLogicalExpr(this)
    }

    data class Set(val `object`: Expr, val name: Token, val value: Expr) : Expr() {
        override fun <R> accept(visitor: Visitor<R>): R = visitor.visitSetExpr(this)
    }

    data class Super(val keyword: Token, val method: Token) : Expr() {
        override fun <R> accept(visitor: Visitor<R>): R = visitor.visitSuperExpr(this)
    }

    data class This(val keyword: Token) : Expr() {
        override fun <R> accept(visitor: Visitor<R>): R = visitor.visitThisExpr(this)
    }

    data class Unary(val operator: Token, val right: Expr) : Expr() {
        override fun <R> accept(visitor: Visitor<R>): R = visitor.visitUnaryExpr(this)
    }

    data class Variable(val name: Token) : Expr() {
        override fun <R> accept(visitor: Visitor<R>): R = visitor.visitVariableExpr(this)
    }
}
