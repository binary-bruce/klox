package klox.ast

import klox.scanner.Token

sealed class Stmt {
    interface Visitor<R> {
        fun <R> visitBlockStmt(stmt: Block): R
        fun <R> visitClassStmt(stmt: Class): R
        fun <R> visitFunctionStmt(stmt: Function): R
        fun <R> visitExpressionStmt(stmt: Expression): R
        fun <R> visitIfStmt(stmt: If): R
        fun <R> visitPrintStmt(stmt: Print): R
        fun <R> visitReturnStmt(stmt: Return): R
        fun <R> visitVarStmt(stmt: Var): R
        fun <R> visitWhileStmt(stmt: While): R
    }

    abstract fun <R> accept(visitor: Visitor<R>): R

    data class Block(val statements: List<Stmt>) : Stmt() {
        override fun <R> accept(visitor: Visitor<R>): R = visitor.visitBlockStmt(this)
    }

    data class Class(val name: Token, val superClass: Expr?, val methods: List<Stmt.Function>) : Stmt() {
        override fun <R> accept(visitor: Visitor<R>): R = visitor.visitClassStmt(this)
    }

    data class Function(val name: Token, val parameters: List<Token>, val body: List<Stmt>) : Stmt() {
        override fun <R> accept(visitor: Visitor<R>): R = visitor.visitFunctionStmt(this)
    }

    data class Expression(val expression: Expr) : Stmt() {
        override fun <R> accept(visitor: Visitor<R>): R = visitor.visitExpressionStmt(this)
    }

    data class If(val condition: Expr, val thenBranch: Stmt, val elseBranch: Stmt?) : Stmt() {
        override fun <R> accept(visitor: Visitor<R>): R = visitor.visitIfStmt(this)
    }

    data class Print(val expression: Expr) : Stmt() {
        override fun <R> accept(visitor: Visitor<R>): R = visitor.visitPrintStmt(this)
    }

    data class Return(val keyword: Token, val value: Expr?) : Stmt() {
        override fun <R> accept(visitor: Visitor<R>): R = visitor.visitReturnStmt(this)
    }

    data class Var(val name: Token, val initializer: Expr?) : Stmt() {
        override fun <R> accept(visitor: Visitor<R>): R = visitor.visitVarStmt(this)
    }

    data class While(val condition: Expr, val body: Stmt) : Stmt() {
        override fun <R> accept(visitor: Visitor<R>): R = visitor.visitWhileStmt(this)
    }
}
