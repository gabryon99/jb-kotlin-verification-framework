## Simple translation from AST to symbolic execution tree

This repository contains the task's implementation for the "*Formal verification framework for Kotlin*". The code
is written using Kotlin 1.7.10, and it has only [dependence](https://github.com/RCHowell/Dotlin) (*Dotlin*) already embedded inside.

### Task Description

Write a function that takes such AST representation and transform it to a **symbolic forward execution tree**, with nodes containing the following values:

- `expr` - next expression to execute
- `S` - symbolic store containing `Expr.Let` expressions over variables, constants or symbolic values (see below)
- `Pi` - path constraints store, containing comparison expressions (`Expr.Eq`, `Expr.NEq`) that are encountered on the branch points for the current path

For more details, see the full description [here](TASK.md).

### Code structure

The code is structured in three packages:

```
|- ast: contains the Abstract Syntax Tree definition for expressions, and an expression reducer
|- symbolic: contains the definition needed for the Excution Symbolic Tree
|- visualizer: contains classes for pretty-printing the Symbolic Tree, using Graphviz
```

### How the Execution Tree is built

The **symbolic forward execution tree** is mainly built by the `ExecutionTreeBuilder.buildNode` function, that works via
pattern matching on the given expression. To the first function call, four parameters are passed:
- `currentExpr`: the expression to which to build the node
- `exprDeque`: a double ended queue for the remaining expressions to build nodes
- `store`: a `SymbolicStore` representing the current variable environment, updated for the next node each time a `Expr.Let` is matched
- `constraints`: a `PathConstraints` representing the current expression guards seen so far, updated for the next node each time a `Expr.If` is matched

Therefore, when `currentExpr` is matched we can have four cases:
1. We're matching an `Expr.Block`, if the `exprDeque` is not empty, we insert all the expressions contained inside the block to a new deque, and we continue the node construction with the new deque.
2. We're matching an `Expr.Let`,  we create a new `SymbolicStore` containing the new info given from the current expression, and we pass it to the new node child. It may happen, as shown in `ExecutionTreeBuilderTest::testBerkeley` and `ExecutionTreeBuilderTest::testWashington`, that an existing symbolic value can be updated. Therefore, we update the current constraints to reflect this event, removing the old constrain related to the variable.
3. We're marching an `Expr.If`, this is the case where the execution path split, therefore it has to be handled carefully. To summarize it, we create two new deques (one for the `then` branch, the other one for `else`). These new deques, have a copy of the current expression's deque, used for building the children. Notice that, the `Expr.If`, as written before, add a new constraints to the current `constraints` data structure.
4. We're matching the remaining expressions, and we just return a new `ExecutionTreeNode`