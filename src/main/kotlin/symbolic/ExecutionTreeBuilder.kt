package symbolic

import ast.Expr
import ast.ExprReducer

class ExecutionTreeBuilder {

    companion object {
        fun astToExecutionTree(ast: Expr): ExecutionTreeNode = ExecutionTreeBuilder().buildNode(ast, ArrayDeque(), SymbolicStore.empty(), PathConstraints.empty())
    }

    fun buildNode(currentExpr: Expr, exprDeque: ArrayDeque<Expr>, store: SymbolicStore, constraints: PathConstraints): ExecutionTreeNode {

        return when (currentExpr) {
            is Expr.Block -> {

                val newDeque = ArrayDeque<Expr>()

                newDeque.addAll(currentExpr.exprs)

                // Drain block expressions to current expression queue
                if (!exprDeque.isEmpty()) {
                    newDeque.addAll(exprDeque)
                }

                buildNode(newDeque.removeFirst(), newDeque, store, constraints)
            }
            is Expr.Let -> {

                val children = mutableListOf<ExecutionTreeNode>()
                if (!exprDeque.isEmpty()) {
                    val newExpr = ExprReducer(store).eval(currentExpr) as Expr.Let
                    val newConstraints = constraints.substitute(newExpr, store.lookup(currentExpr.variable))
                    val child = buildNode(exprDeque.removeFirst(), exprDeque, store.substitute(newExpr), newConstraints)
                    children.add(child)
                }

                ExecutionTreeNode(children, currentExpr, store, constraints)
            }
            is Expr.If -> {

                val children = mutableListOf<ExecutionTreeNode>().apply {
                    val thenDeque = ArrayDeque<Expr>().apply {
                        addFirst(currentExpr.thenExpr)
                        addAll(exprDeque)
                    }
                    val reducedExpr = ExprReducer(store).eval(currentExpr.cond) as Expr.Comparison
                    val child = buildNode(thenDeque.removeFirst(), thenDeque, store, constraints.addConstraint(reducedExpr))
                    add(child)
                }

                if (currentExpr.elseExpr != null) {

                    val elseDeque = ArrayDeque<Expr>().apply {
                        add(currentExpr.elseExpr)
                        addAll(exprDeque)
                    }

                    if (currentExpr.cond is Expr.Comparison) {
                        val reducedExpr = ExprReducer(store).eval(currentExpr.cond.negate()) as Expr.Comparison
                        children.add(buildNode(elseDeque.removeFirst(), elseDeque, store, constraints.addConstraint(reducedExpr)))
                    }

                }
                else {
                    if (currentExpr.cond is Expr.Comparison) {
                        val reducedExpr = ExprReducer(store).eval(currentExpr.cond.negate()) as Expr.Comparison
                        children.add(buildNode(exprDeque.removeFirst(), exprDeque, store, constraints.addConstraint(reducedExpr)))
                    }
                }

                ExecutionTreeNode(children, currentExpr, store, constraints)
            }
            else -> {
                ExecutionTreeNode(listOf(), currentExpr, store, constraints)
            }
        }
    }
}
