package klox.ast

import klox.scanner.Token

sealed class Stmt {
    interface Visitor<R> {
        fun <R> visit(expr: Block): R
        fun <R> visit(expr: Class): R
        fun <R> visit(expr: Function): R
        fun <R> visit(expr: Expression): R
        fun <R> visit(expr: If): R
        fun <R> visit(expr: Print): R
        fun <R> visit(expr: Return): R
        fun <R> visit(expr: Var): R
        fun <R> visit(expr: While): R
    }

    abstract fun <R> accept(visitor: Visitor<R>): R

    data class Block(val statements: List<Stmt>) : Stmt() {
        override fun <R> accept(visitor: Visitor<R>): R = visitor.visit(this)
    }

    data class Class(val name: Token, val superClass: Expr?, val methods: List<Stmt.Function>) : Stmt() {
        override fun <R> accept(visitor: Visitor<R>): R = visitor.visit(this)
    }

    data class Function(val name: Token, val parameters: List<Token>, val body: List<Stmt>) : Stmt() {
        override fun <R> accept(visitor: Visitor<R>): R = visitor.visit(this)
    }

    data class Expression(val expression: Expr) : Stmt() {
        override fun <R> accept(visitor: Visitor<R>): R = visitor.visit(this)
    }

    data class If(val condition: Expr, val thenBranch: Stmt, val elseBranch: Stmt?) : Stmt() {
        override fun <R> accept(visitor: Visitor<R>): R = visitor.visit(this)
    }

    data class Print(val expression: Expr) : Stmt() {
        override fun <R> accept(visitor: Visitor<R>): R = visitor.visit(this)
    }

    data class Return(val keyword: Token, val value: Expr?) : Stmt() {
        override fun <R> accept(visitor: Visitor<R>): R = visitor.visit(this)
    }

    data class Var(val name: Token, val initializer: Expr?) : Stmt() {
        override fun <R> accept(visitor: Visitor<R>): R = visitor.visit(this)
    }

    data class While(val condition: Expr, val body: Stmt) : Stmt() {
        override fun <R> accept(visitor: Visitor<R>): R = visitor.visit(this)
    }
}
