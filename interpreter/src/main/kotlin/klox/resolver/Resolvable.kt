package klox.resolver

import klox.ast.Expr

interface Resolvable {
    fun resolve(expr: Expr, depth: Int)
}
