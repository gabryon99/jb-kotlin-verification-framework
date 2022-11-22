package symbolic

import ast.Expr
import java.util.StringJoiner

/**
 * A *Symbolic Store* associates program variables with either expressions over concrete values or symbolic values.
 */
class SymbolicStore(private val vars: List<Expr.Let>) {

    companion object {
        fun empty() : SymbolicStore {
            return SymbolicStore(listOf())
        }
    }

    fun substitute(let: Expr.Let): SymbolicStore {

        val copy = vars.toMutableList()
        copy.removeIf {
            it.variable.name == let.variable.name
        }
        copy.add(let)

        return SymbolicStore(copy)
    }

    override fun toString(): String {

        val exprListString = vars.fold(StringJoiner(",")) { acc: StringJoiner, exp: Expr ->
            acc.add("[$exp]")
        }

        return "\\{$exprListString\\}"
    }

    fun lookup(e: Expr.Var): Expr.Let = vars.find { e.name == it.variable.name }!!
}