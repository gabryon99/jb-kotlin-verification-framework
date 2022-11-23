package symbolic

import ast.Expr
import org.junit.jupiter.api.Test
import visualizer.GraphvizVisualizer

internal class ExecutionTreeBuilderTest {

    fun printGraphvizCode(tree: ExecutionTreeNode) {
        println(GraphvizVisualizer.drawTree(tree))
    }

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

        printGraphvizCode(ExecutionTreeBuilder.astToExecutionTree(fooBarAst))
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
        printGraphvizCode(ExecutionTreeBuilder.astToExecutionTree(ast))
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
        printGraphvizCode(ExecutionTreeBuilder.astToExecutionTree(ast))
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
        printGraphvizCode(ExecutionTreeBuilder.astToExecutionTree(ast))
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
        printGraphvizCode(ExecutionTreeBuilder.astToExecutionTree(ast))
    }

    @Test
    fun testBerkeley() {
        // Code taken from: https://inst.eecs.berkeley.edu/~cs261/fa17/scribe/SymbolicExecution.pdf
        val ast = Expr.Block(
            Expr.Let(Expr.Var("x"), Expr.SymVal("x0")),
            Expr.Let(Expr.Var("y"), Expr.SymVal("y0")),
            Expr.Let(Expr.Var("z"), Expr.Mul(Expr.Const(2), Expr.Var("x"))),
            Expr.Let(Expr.Var("k"), Expr.Const(3)),
            Expr.If(Expr.Comparison.Gt(Expr.Var("z"), Expr.Var("k")),
                Expr.Block(
                    Expr.If(Expr.Comparison.Lt(Expr.Var("y"), Expr.Var("z")),
                        Expr.Const(-1), // EXIT_FAILURE
                        Expr.Comparison.NEq(Expr.Var("y"), Expr.Var("z"))
                    )
                )
            ),
            Expr.Const(0) // Since the function shown is void, we append a fake `return 0`
        )
        printGraphvizCode(ExecutionTreeBuilder.astToExecutionTree(ast))
    }

    @Test
    fun testWashington() {
        // Code taken from: https://courses.cs.washington.edu/courses/cse403/16au/lectures/L16.pdf
        val ast = Expr.Block(
            Expr.Let(Expr.Var("x"), Expr.SymVal("x0")),
            Expr.Let(Expr.Var("y"), Expr.SymVal("y0")),
            Expr.If(Expr.Comparison.Gt(Expr.Var("x"), Expr.Var("y")), Expr.Block(
                Expr.Let(Expr.Var("x"), Expr.Plus(Expr.Var("x"), Expr.Var("y"))),
                Expr.Let(Expr.Var("y"), Expr.Minus(Expr.Var("x"), Expr.Var("y"))),
                Expr.Let(Expr.Var("x"), Expr.Minus(Expr.Var("x"), Expr.Var("y"))),
                Expr.If(Expr.Comparison.Gt(Expr.Minus(Expr.Var("x"), Expr.Var("y")), Expr.Const(0)), Expr.Const(0))
            )),
            Expr.Const(1)
        )
        printGraphvizCode(ExecutionTreeBuilder.astToExecutionTree(ast))
    }

}