package ast

import java.util.StringJoiner

sealed class Expr {

    /***
     * A binding name expression, with string as identifiers and
     * an initial expression value. (The assignment operator)
     */
    class Let(val variable: Var, val value: Expr) : Expr() {
        override fun toString(): String {
            return "$variable = $value"
        }
    }
    /***
     * Represents a block of expressions.
     */
    class Block(vararg val exprs: Expr) : Expr() {
        override fun toString(): String {
            return exprs.fold(StringJoiner("\n")) { acc: StringJoiner, exp: Expr ->
                acc.add(exp.toString())
            }.toString()
        }
    }

    /***
     * ast.If expressions, if the condition holds true, then the `thenExpr` is evaluated,
     * otherwise `elseExpr` (that is optional).
     */
    class If(val cond: Expr, val thenExpr: Expr, val elseExpr: Expr? = null) : Expr() {
        override fun toString(): String {

            var str = "if ($cond)"
            if (elseExpr != null) str += " else"

            return str
        }
    }

    /***
     * A constant expression.
     */
    class Const(val value: Int) : Expr() {
        override fun toString(): String = value.toString()
    }
    /***
     * A variable expression, with strings as identifiers.
     */
    class Var(val name: String) : Expr() {
        override fun toString(): String = name
    }
    /***
     * An equal comparison between two expressions.
     */
    class Eq(val left: Expr, val right: Expr) : Expr() {
        fun negate(): NEq {
            return NEq(left, right)
        }

        override fun toString(): String {
            return "$left == $right"
        }
    }

    /***
     * A not-equal comparison between two expressions.
     */
    class NEq(val left: Expr, val right: Expr) : Expr() {
        fun negate(): Eq {
            return Eq(left, right)
        }

        override fun toString(): String {
            return "$left ≠ $right"
        }
    }

    /***
     * Addition expression.
     */
    class Plus(val left: Expr, val right: Expr) : Expr()  {
        override fun toString(): String {
            return "$left + $right"
        }
    }

    /***
     * Subtraction expression.
     */
    class Minus(val left: Expr, val right: Expr) : Expr() {
        override fun toString(): String {
            return "$left - $right"
        }
    }

    /***
     * Multiplication expression.
     */
    class Mul(val left: Expr, val right: Expr) : Expr() {
        override fun toString(): String {
            return "$left * $right"
        }
    }

    /***
     * Symbolic value.
     */
    class SymVal(val name: String) : Expr() {
        override fun toString(): String = name
    }

}
