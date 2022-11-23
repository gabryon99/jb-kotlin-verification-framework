package symbolic

import ast.Expr
import org.junit.jupiter.api.Test
import visualizer.GraphvizVisualizer

internal class ExecutionTreeBuilderTest {

    @Test
    fun testPaperExample() {

        val fooBarAst = Expr.Block(
            Expr.Let(Expr.Var("a"), Expr.SymVal("α")),
            Expr.Let(Expr.Var("b"), Expr.SymVal("β")),
            Expr.Let(Expr.Var("x"), Expr.Const(1)),
            Expr.Let(Expr.Var("y"), Expr.Const(0)),
            Expr.If(
                Expr.Comparison.NEq(Expr.Var("a"), Expr.Const(0)),
                Expr.Block(
                    Expr.Let(Expr.Var("y"), Expr.Plus(Expr.Const(3), Expr.Var("x"))),
                    Expr.If(Expr.Comparison.Eq(Expr.Var("b"), Expr.Const(0)),
                        Expr.Let(Expr.Var("x"), Expr.Mul(Expr.Const(2), Expr.Plus(Expr.Var("a"), Expr.Var("b")))),
                    )
                ),
            ),
            Expr.Minus(Expr.Var("x"), Expr.Var("y"))
        )

        val tree = ExecutionTreeBuilder.astToExecutionTree(fooBarAst)

        println(GraphvizVisualizer.drawTree(tree))
    }


}