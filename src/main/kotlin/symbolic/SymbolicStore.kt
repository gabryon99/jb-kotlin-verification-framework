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

    /**
     * Given a let expressions, returns a new SymbolicStore
     * where the occurrence of given let are replaced with the existing one.
     */
    fun substitute(let: Expr.Let): SymbolicStore {

        if (let.variable.name.isEmpty()) {
            return this
        }

        val copy = vars.toMutableList().apply{
            removeIf {
                it.variable.name == let.variable.name
            }
            add(let)
        }

        return SymbolicStore(copy)
    }

    override fun toString(): String {

        val exprListString = vars.fold(StringJoiner(",")) { acc: StringJoiner, exp: Expr ->
            acc.add("[$exp]")
        }

        return "\\{$exprListString\\}"
    }

    /***
     * Given a variable expression, return its associated value
     */
    fun lookup(e: Expr.Var): Expr.Let {
        return vars.find { e.name == it.variable.name } ?: Expr.Let(Expr.Var(""), Expr.Const(0))
    }
}
