package klox

import klox.scanner.Token

object InterpreterUtils {
    fun checkNumberOperand(operator: Token, operand: Any) {
        if (operand is Double) return
        throw RuntimeError(operator, "Operand must be a number.")
    }

    fun checkNumberOperands(operator: Token, left: Any, right: Any) {
        if (left is Double && right is Double) return
        throw RuntimeError(operator, "Operands must be numbers.")
    }

    fun isTruthy(`object`: Any?): Boolean {
        if (`object` == null) return false
        return if (`object` is Boolean) `object` else true
    }

    fun isEqual(a: Any?, b: Any?): Boolean {
        // nil is only equal to nil.
        if (a == null && b == null) return true
        return if (a == null) false else a == b
    }

    fun stringify(`object`: Any?): String {
        if (`object` == null) return "nil"

        // Hack. Work around Java adding ".0" to integer-valued doubles.
        return if (`object` is Double) {
            var text = `object`.toString()
            if (text.endsWith(".0")) {
                text = text.substring(0, text.length - 2)
            }
            text
        } else {
            `object`.toString()
        }
    }
}
