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

    sealed class Comparison: Expr() {

        abstract fun negate(): Expr

        class Gt(val left: Expr, val right: Expr) : Comparison() {
            override fun negate(): Lte = Lte(left, right)
            override fun toString(): String = "$left \\> $right"
        }

        class Lt(val left: Expr, val right: Expr) : Comparison() {
            override fun negate(): Gte = Gte(left, right)
            override fun toString(): String = "$left \\< $right"
        }

        class Gte(val left: Expr, val right: Expr) : Comparison() {
            override fun negate(): Lt = Lt(left, right)
            override fun toString(): String = "$left \\>= $right"
        }

        class Lte(val left: Expr, val right: Expr) : Comparison() {
            override fun negate(): Gt = Gt(left, right)
            override fun toString(): String = "$left \\<= $right"
        }

        /***
         * An equal comparison between two expressions.
         */
        class Eq(val left: Expr, val right: Expr) : Comparison() {
            override fun negate(): NEq = NEq(left, right)
            override fun toString(): String = "$left == $right"
        }

        /***
         * A not-equal comparison between two expressions.
         */
        class NEq(val left: Expr, val right: Expr) : Comparison() {
            override fun negate(): Eq = Eq(left, right)
            override fun toString(): String = "$left ??? $right"
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
