package symbolic

import ast.Expr
import java.util.*

/**
 * Path constraints*, i.e., is a formula that expresses a set of assumptions on the symbols αi due to branches taken in
 * the execution to reach stmt.
*/
class PathConstraints(private val constraints: List<Expr>) {

    companion object {
        fun empty() : PathConstraints {
            return PathConstraints(listOf())
        }
    }

    /**
     * Return a new PathConstraints containing the given expressions and the
     * previous ones.
     */
    fun addConstraint(expr: Expr): PathConstraints {
        return PathConstraints(mutableListOf(expr).apply {
            addAll(constraints)
        })
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
