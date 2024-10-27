package klox.ast

import klox.scanner.Token

sealed class Expr {
    interface Visitor<R> {
        fun <R> visit(expr: Assign): R
        fun <R> visit(expr: Binary): R
        fun <R> visit(expr: Call): R
        fun <R> visit(expr: Get): R
        fun <R> visit(expr: Grouping): R
        fun <R> visit(expr: Literal): R
        fun <R> visit(expr: Logical): R
        fun <R> visit(expr: Set): R
        fun <R> visit(expr: Super): R
        fun <R> visit(expr: This): R
        fun <R> visit(expr: Unary): R
        fun <R> visit(expr: Variable): R
    }

    abstract fun <R> accept(visitor: Visitor<R>): R

    data class Assign(val name: Token, val value: Expr) : Expr() {
        override fun <R> accept(visitor: Visitor<R>): R = visitor.visit(this)
    }

    data class Binary(val left: Expr, val operator: Token, val right: Expr) : Expr() {
        override fun <R> accept(visitor: Visitor<R>): R = visitor.visit(this)
    }

    data class Call(val callee: Expr, val paren: Token, val arguments: List<Expr>) : Expr() {
        override fun <R> accept(visitor: Visitor<R>): R = visitor.visit(this)
    }

    data class Get(val `object`: Expr, val name: Token) : Expr() {
        override fun <R> accept(visitor: Visitor<R>): R = visitor.visit(this)
    }

    data class Grouping(val expression: Expr) : Expr() {
        override fun <R> accept(visitor: Visitor<R>): R = visitor.visit(this)
    }

    data class Literal(val value: Any?) : Expr() {
        override fun <R> accept(visitor: Visitor<R>): R = visitor.visit(this)
    }

    data class Logical(val left: Expr, val operator: Token, val right: Expr) : Expr() {
        override fun <R> accept(visitor: Visitor<R>): R = visitor.visit(this)
    }

    data class Set(val `object`: Expr, val name: Token, val value: Expr) : Expr() {
        override fun <R> accept(visitor: Visitor<R>): R = visitor.visit(this)
    }

    data class Super(val keyword: Token, val method: Token) : Expr() {
        override fun <R> accept(visitor: Visitor<R>): R = visitor.visit(this)
    }

    data class This(val keyword: Token) : Expr() {
        override fun <R> accept(visitor: Visitor<R>): R = visitor.visit(this)
    }

    data class Unary(val operator: Token, val right: Expr) : Expr() {
        override fun <R> accept(visitor: Visitor<R>): R = visitor.visit(this)
    }

    data class Variable(val name: Token) : Expr() {
        override fun <R> accept(visitor: Visitor<R>): R = visitor.visit(this)
    }
}
