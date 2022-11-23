package symbolic

import ast.Expr
import java.util.*

/**
 * Path constraints*, i.e., is a formula that expresses a set of assumptions on the symbols αi due to branches taken in
 * the execution to reach stmt.
*/
class PathConstraints(private val constraints: List<Expr.Comparison>) {

    companion object {
        fun empty() : PathConstraints {
            return PathConstraints(listOf())
        }
    }

    /**
     * Return a new PathConstraints containing the given expressions and the
     * previous ones.
     */
    fun addConstraint(expr: Expr.Comparison): PathConstraints {
        return PathConstraints(mutableListOf(expr).apply {
            addAll(constraints)
        })
    }

    fun substitute(newLet: Expr.Let, prevLet: Expr.Let): PathConstraints {

        // Does the given expression override some existing symbolic value constraint?
        // If yes, remove it, otherwise don't do anything

        if (newLet.variable.name.isEmpty() || prevLet.value !is Expr.SymVal) {
            return this
        }
        if (newLet.variable.name != prevLet.variable.name) {
            return this
        }

        val copy = constraints.toMutableList().apply{
            removeIf {
                when (it) {
                    is Expr.Comparison.Gt   -> (it.left == prevLet.value || it.right == prevLet.value)
                    is Expr.Comparison.Gte  -> (it.left == prevLet.value || it.right == prevLet.value)
                    is Expr.Comparison.Lt   -> (it.left == prevLet.value || it.right == prevLet.value)
                    is Expr.Comparison.Lte  -> (it.left == prevLet.value || it.right == prevLet.value)
                    is Expr.Comparison.Eq   -> (it.left == prevLet.value || it.right == prevLet.value)
                    is Expr.Comparison.NEq  -> (it.left == prevLet.value || it.right == prevLet.value)
                }
            }
        }

        return PathConstraints(copy)

    }

    override fun toString(): String {

        if (constraints.isEmpty()) {
            return "\\{true\\}"
        }

        val exprListString = constraints.fold(StringJoiner(",")) { acc: StringJoiner, exp: Expr ->
            acc.add("[$exp]")
        }

        return "\\{$exprListString\\}"
    }

}
