package symbolic

import ast.Expr

/**
 * Class representing a tree node inside the symbolic forward execution tree.
 * The class is a tuple formed by: (stmt, \sigma, \pi)
 */
class ExecutionTreeNode(
    val children: List<ExecutionTreeNode>,
    val expr: Expr?,
    val sigma: SymbolicStore = SymbolicStore.empty(),
    val pi: PathConstraints = PathConstraints.empty()
) {
    override fun toString(): String {
        return "Node(children=${children.size}, expr=[$expr], sigma=$sigma, pi=$pi)"
    }
}