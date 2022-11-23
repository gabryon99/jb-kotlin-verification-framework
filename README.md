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
