package ast

import symbolic.SymbolicStore

class ExprReducer(private val store: SymbolicStore) {

    fun eval(e: Expr): Expr {
        return when (e) {
            is Expr.Var -> {
                store.lookup(e).value
            }
            is Expr.Comparison.Eq -> {
                return Expr.Comparison.Eq(eval(e.left), eval(e.right))
            }
            is Expr.Comparison.NEq -> {
                return Expr.Comparison.NEq(eval(e.left), eval(e.right))
            }
            is Expr.Plus -> {
                val lhs = eval(e.left)
                val rhs = eval(e.right)
                if (lhs is Expr.Const && rhs is Expr.Const) {
                    return Expr.Const(lhs.value + rhs.value)
                }
                return Expr.Plus(lhs, rhs)
            }
            is Expr.Minus -> {
                val lhs = eval(e.left)
                val rhs = eval(e.right)
                if (lhs is Expr.Const && rhs is Expr.Const) {
                    return Expr.Const(lhs.value - rhs.value)
                }
                return Expr.Minus(lhs, rhs)
            }
            is Expr.Mul -> {
                val lhs = eval(e.left)
                val rhs = eval(e.right)
                if (lhs is Expr.Const && rhs is Expr.Const) {
                    return Expr.Const(lhs.value * rhs.value)
                }
                return Expr.Mul(lhs, rhs)
            }
            is Expr.Let -> {
                return Expr.Let(e.variable, eval(e.value))
            }
            else -> e
        }
    }
}