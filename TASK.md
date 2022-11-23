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

```kotlin
sealed class Expr

class Block(vararg val exprs: Expr) : Expr()

class Const(val value: Int) : Expr()
class Var(val name: String) : Expr()
class Let(val variable: Var, val value: Expr) : Expr()
class Eq(val left: Expr, val right: Expr) : Expr()
class NEq(val left: Expr, val right: Expr) : Expr()
class If(val cond: Expr, val thenExpr: Expr, val elseExpr: Expr? = null) : Expr()
class Plus(val left: Expr, val right: Expr) : Expr()
class Minus(val left: Expr, val right: Expr) : Expr()
class Mul(val left: Expr, val right: Expr) : Expr()
```

The following example Kotlin function:

```kotlin
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

```kotlin
val fooBarAst = Expr.Block(
    Expr.Let(Expr.Var("x"), Expr.Const(1)),
    Expr.Let(Expr.Var("y"), Expr.Const(0)),
    Expr.If(Expr.NEq(Expr.Var("a"), Expr.Const(0)),
        Expr.Block(
            Expr.Let(Expr.Var("y"), Expr.Plus(Expr.Const(3), Expr.Var("x"))),
            Expr.If( Expr.Eq(Expr.Var("b"), Expr.Const(0)),
                Expr.Let(Expr.Var("x"), Expr.Mul(Expr.Const(2), Expr.Plus(Expr.Var("a"), Expr.Var("b")))),
            )
        )
    ),
    Expr.Minus(Expr.Var("x"), Expr.Var("y"))
)
```

Write a function that takes such AST representation and transform it to a **symbolic forward execution tree**, with nodes containing the following values:

- `expr` - next expression to execute
- `S` - symbolic store containing `Expr.Let` expressions over variables, constants or symbolic values (see below)
- `Pi` - path constraints store, containing comparison expressions (`Expr.Eq`, `Expr.NEq`) that are encountered on the branch points for the current path

You can reuse the `Expr.Expr` hierarchy for the `S` and `Pi`, but you'll need one more element for the symbolic values:

```kotlin
class SymVal(val name: String) : Expr()
```

You can use the following class to represent the tree nodes:

```kotlin
class ExecTreeNode(
    val children: List<ExecTreeNode>,
    val nextExpr: Expr,
    val S: List<Expr>,
    val Pi: List<Expr>
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