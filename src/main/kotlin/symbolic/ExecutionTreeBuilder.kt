package symbolic

import ast.Expr
import ast.ExprReducer
import visualizer.GraphvizVisualizer

class ExecutionTreeBuilder {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {

            val fooBarAst = Expr.Block(
                Expr.Let(Expr.Var("a"), Expr.SymVal("α")),
                Expr.Let(Expr.Var("b"), Expr.SymVal("β")),
                Expr.Let(Expr.Var("x"), Expr.Const(1)),
                Expr.Let(Expr.Var("y"), Expr.Const(0)),
                Expr.If(Expr.NEq(Expr.Var("a"), Expr.Const(0)),
                    Expr.Block(
                        Expr.Let(Expr.Var("y"), Expr.Plus(Expr.Const(3), Expr.Var("x"))),
                        Expr.If( Expr.Eq(Expr.Var("b"), Expr.Const(0)),
                            Expr.Let(Expr.Var("x"), Expr.Mul(Expr.Const(2), Expr.Plus(Expr.Var("a"), Expr.Var("b")))),
                        )
                    ),
                    Expr.Block(
                        Expr.Let(Expr.Var("z"), Expr.Const(42)),
                    )
                ),
                Expr.Minus(Expr.Var("x"), Expr.Var("y"))
            )
            val tree = ExecutionTreeBuilder().buildNode(fooBarAst, ArrayDeque(), SymbolicStore.empty(), PathConstraints.empty())
            println(GraphvizVisualizer.drawTree(tree))
        }
    }

    fun buildNode(currentExpr: Expr, exprDeque: ArrayDeque<Expr>, store: SymbolicStore, constraints: PathConstraints): ExecutionTreeNode {

        return when (currentExpr) {
            is Expr.Block -> {

                val newQueue = ArrayDeque<Expr>()

                newQueue.addAll(currentExpr.exprs)

                // Drain block expressions to current expression queue
                if (!exprDeque.isEmpty()) {
                    newQueue.addAll(exprDeque)
                }

                buildNode(newQueue.removeFirst(), newQueue, store, constraints)
            }
            is Expr.Let -> {

                val children = mutableListOf<ExecutionTreeNode>()
                if (!exprDeque.isEmpty()) {
                    val newExpr = ExprReducer(store).eval(currentExpr) as Expr.Let
                    val child = buildNode(exprDeque.removeFirst(), exprDeque, store.substitute(newExpr), constraints)
                    children.add(child)
                }

                ExecutionTreeNode(children, currentExpr, store, constraints)
            }
            is Expr.If -> {
                
                val children = mutableListOf<ExecutionTreeNode>().apply {
                    val thenQueue = ArrayDeque<Expr>().apply {
                        addFirst(currentExpr.thenExpr)
                        addAll(exprDeque)
                    }
                    val reducedExpr = ExprReducer(store).eval(currentExpr.cond)
                    val child = buildNode(thenQueue.removeFirst(), thenQueue, store, constraints.addConstraint(reducedExpr))
                    add(child)
                }

                if (currentExpr.elseExpr != null) {

                    val elseQueue = ArrayDeque<Expr>().apply {
                        add(currentExpr.elseExpr)
                        addAll(exprDeque)
                    }

                    if (currentExpr.cond is Expr.Eq) {
                        val reducedExpr = ExprReducer(store).eval(currentExpr.cond) as Expr.Eq
                        val child = buildNode(elseQueue.removeFirst(), elseQueue, store, constraints.addConstraint(reducedExpr.negate()))
                        children.add(child)
                    }
                    else if (currentExpr.cond is Expr.NEq) {
                        val reducedExpr = ExprReducer(store).eval(currentExpr.cond) as Expr.NEq
                        val child = buildNode(elseQueue.removeFirst(), elseQueue, store, constraints.addConstraint(reducedExpr.negate()))
                        children.add(child)
                    }

                }
                else {
                    if (currentExpr.cond is Expr.Eq) {
                        val reducedExpr = ExprReducer(store).eval(currentExpr.cond) as Expr.Eq
                        children.add(buildNode(exprDeque.removeFirst(), exprDeque, store, constraints.addConstraint(reducedExpr.negate())))
                    }
                    else if (currentExpr.cond is Expr.NEq) {
                        val reducedExpr = ExprReducer(store).eval(currentExpr.cond) as Expr.NEq
                        children.add(buildNode(exprDeque.removeFirst(), exprDeque, store, constraints.addConstraint(reducedExpr.negate())))
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
