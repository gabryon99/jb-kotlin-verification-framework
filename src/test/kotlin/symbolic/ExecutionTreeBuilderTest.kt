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

    @Test
    fun testGreaterThan() {
        val ast = Expr.Block(
            Expr.Let(Expr.Var("a"), Expr.SymVal("α")),
            Expr.Let(Expr.Var("b"), Expr.SymVal("β")),
            Expr.Let(Expr.Var("x"), Expr.Const(0)),
            Expr.Let(Expr.Var("y"), Expr.Const(0)),
            Expr.If(Expr.Comparison.Gt(Expr.Var("a"), Expr.Var("b")),
                Expr.Let(Expr.Var("x"), Expr.Const(40)),
                Expr.Let(Expr.Var("y"), Expr.Const(2))),
            Expr.Plus(Expr.Var("x"), Expr.Var("y"))
        )
        val tree = ExecutionTreeBuilder.astToExecutionTree(ast)
        println(GraphvizVisualizer.drawTree(tree))
    }

    @Test
    fun testGreaterOrEqualThan() {
        val ast = Expr.Block(
            Expr.Let(Expr.Var("a"), Expr.SymVal("α")),
            Expr.Let(Expr.Var("b"), Expr.SymVal("β")),
            Expr.Let(Expr.Var("x"), Expr.Const(0)),
            Expr.Let(Expr.Var("y"), Expr.Const(0)),
            Expr.If(Expr.Comparison.Gte(Expr.Var("a"), Expr.Var("b")),
                Expr.Let(Expr.Var("x"), Expr.Const(40)),
                Expr.Let(Expr.Var("y"), Expr.Const(2))),
            Expr.Plus(Expr.Var("x"), Expr.Var("y"))
        )
        val tree = ExecutionTreeBuilder.astToExecutionTree(ast)
        println(GraphvizVisualizer.drawTree(tree))
    }

    @Test
    fun testLessThan() {
        val ast = Expr.Block(
            Expr.Let(Expr.Var("a"), Expr.SymVal("α")),
            Expr.Let(Expr.Var("b"), Expr.SymVal("β")),
            Expr.Let(Expr.Var("x"), Expr.Const(0)),
            Expr.Let(Expr.Var("y"), Expr.Const(0)),
            Expr.If(Expr.Comparison.Lt(Expr.Var("a"), Expr.Var("b")),
                Expr.Let(Expr.Var("x"), Expr.Const(40)),
                Expr.Let(Expr.Var("y"), Expr.Const(2))),
            Expr.Plus(Expr.Var("x"), Expr.Var("y"))
        )
        val tree = ExecutionTreeBuilder.astToExecutionTree(ast)
        println(GraphvizVisualizer.drawTree(tree))
    }

    @Test
    fun testLessOrEqualThan() {
        val ast = Expr.Block(
            Expr.Let(Expr.Var("a"), Expr.SymVal("α")),
            Expr.Let(Expr.Var("b"), Expr.SymVal("β")),
            Expr.Let(Expr.Var("x"), Expr.Const(0)),
            Expr.Let(Expr.Var("y"), Expr.Const(0)),
            Expr.If(Expr.Comparison.Lte(Expr.Var("a"), Expr.Var("b")),
                Expr.Let(Expr.Var("x"), Expr.Const(40)),
                Expr.Let(Expr.Var("y"), Expr.Const(2))),
            Expr.Plus(Expr.Var("x"), Expr.Var("y"))
        )
        val tree = ExecutionTreeBuilder.astToExecutionTree(ast)
        println(GraphvizVisualizer.drawTree(tree))
    }

    @Test
    fun testUpdateConstraints() {
        val ast = Expr.Block(
            Expr.Let(Expr.Var("a"), Expr.SymVal("α")),
            Expr.Let(Expr.Var("b"), Expr.SymVal("β")),
            Expr.If(Expr.Comparison.Lte(Expr.Var("a"), Expr.Var("b")),
                Expr.Let(Expr.Var("a"), Expr.Const(40)),
                Expr.Let(Expr.Var("b"), Expr.Const(2))),
            Expr.Plus(Expr.Var("a"), Expr.Var("b"))
        )
        val tree = ExecutionTreeBuilder.astToExecutionTree(ast)
        println(GraphvizVisualizer.drawTree(tree))
    }


}