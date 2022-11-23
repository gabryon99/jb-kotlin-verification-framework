## Simple translation from AST to symbolic execution tree

This repository contains the task's implementation for the "*Formal verification framework for Kotlin*". The code
is written using Kotlin 1.7.10, and it has only [dependence](https://github.com/RCHowell/Dotlin) (*Dotlin*) already embedded inside.

### Code structure

The code is structured in three packages:

```
|- ast: contains the Abstract Syntax Tree definition for expressions, and an expression reducer
|- symbolic: contains the definition needed for the Excution Symbolic Tree
|- visualizer: contains classes for pretty-printing the Symbolic Tree, using Graphviz
```
