## Implement simple translation from AST to symbolic execution tree

### Overview

- We don't expect this assignment to take more than a day of work to complete, but if you're having fun with it, there is also a bonus section below. 
- If you find an instruction to be ambiguous, use your judgement on how to solve it. 
- You're encouraged to use any resources you find online.
- You're encouraged to implement the assignment in Kotlin. In this case, you can take the infrastructure and example code below as is and add your implementation on top of it. The minimal acceptable solution may consist of only one source file. But having a minimal project structure (especially if tests are submitted too) will simplify the evaluation.
- We might accept reasonable submissions written in other languages (e.g. Java, Scala, C# and maybe others), but you'll need to convert the base code below yourself, and we may ask you to provide additional description on how to run it, if it will not be obvious.
- Please comment your code where necessary to explain your approach to the problem.

### Description

Taking the following Kotlin code as a base:

```
sealed class ast.Expr

class ast.Block(vararg val exprs: ast.Expr) : ast.Expr()

class ast.Const(val value: Int) : ast.Expr()
class ast.Var(val name: String) : ast.Expr()
class ast.Let(val variable: ast.Var, val value: ast.Expr) : ast.Expr()
class ast.Eq(val left: ast.Expr, val right: ast.Expr) : ast.Expr()
class ast.NEq(val left: ast.Expr, val right: ast.Expr) : ast.Expr()
class ast.If(val cond: ast.Expr, val thenExpr: ast.Expr, val elseExpr: ast.Expr? = null) : ast.Expr()
class ast.Plus(val left: ast.Expr, val right: ast.Expr) : ast.Expr()
class ast.Minus(val left: ast.Expr, val right: ast.Expr) : ast.Expr()
class ast.Mul(val left: ast.Expr, val right: ast.Expr) : ast.Expr()
```

The following example Kotlin function:

```
fun foobar(a: Int, b: Int): Int {
    var x = 1
    var y = 0
    if (a != 0) {
        y = 3 + x
        if (b == 0) {
            x = 2 * (a + b)
        }
    }
    return x - y
}
```

Could be represented as the following AST (abstract syntax tree):

```
val fooBarAst = ast.Block(
    ast.Let(ast.Var("x"), ast.Const(1)),
    ast.Let(ast.Var("y"), ast.Const(0)),
    ast.If(ast.NEq(ast.Var("a"), ast.Const(0)),
        ast.Block(
            ast.Let(ast.Var("y"), ast.Plus(ast.Const(3), ast.Var("x"))),
            ast.If( ast.Eq(ast.Var("b"), ast.Const(0)),
                ast.Let(ast.Var("x"), ast.Mul(ast.Const(2), ast.Plus(ast.Var("a"), ast.Var("b")))),
            )
        )
    ),
    ast.Minus(ast.Var("x"), ast.Var("y"))
)
```

Write a function that takes such AST representation and transform it to a **symbolic forward execution tree**, with nodes containing the following values:

- `expr` - next expression to execute
- `S` - symbolic store containing `ast.Let` expressions over variables, constants or symbolic values (see below)
- `Pi` - path constraints store, containing comparison expressions (`ast.Eq`, `ast.NEq`) that are encountered on the branch points for the current path

You can reuse the `ast.Expr` hierarchy for the `S` and `Pi`, but you'll need one more element for the symbolic values:

```
class SymVal(val name: String) : ast.Expr()
```

You can use the following class to represent the tree nodes:

```
class ExecTreeNode(
    val children: List<ExecTreeNode>,
    val nextExpr: ast.Expr,
    val S: List<ast.Expr>,
    val Pi: List<ast.Expr>
)
```

Or implement your own variant.

The example code is adapted from the paper *A Survey of Symbolic Execution Techniques*, Section 1.1 - A Warm-Up Example. 
You can read more detailed description of the symbolic execution tree construction there, as well 
as check the expected resulting tree on the Fig 2 (Only "main" nodes are expected, no decision nodes since no SMT solver
is required for this test task. Also, the `assert` node from the paper is replaced here with the simpler last "return" expression.)

But of course, the resulting function should accept any reasonable AST as an input, not only the example above.

*Hint*: you'll need a "substitute" function that can take an expression and produce a copy of it, 
with all instances of the variable replaced with another expression.

### Bonus tasks
- [x] Implement a pretty printer for the execution tree.
- [ ] Write simple tests with the given example AST, comparing the results with pretty printed execution tree.
- [ ] Extend the Expr hierarchy with more arithmetic and logic operators, write tests that use new operators.
- [ ] **Extra bonus** - connect an SMT solver to the project and evaluate constraints as described in the paper above.