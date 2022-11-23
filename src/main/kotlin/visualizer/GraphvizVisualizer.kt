package visualizer

import ast.Expr
import io.github.rchowell.dotlin.DotNodeShape
import io.github.rchowell.dotlin.digraph
import symbolic.ExecutionTreeNode

class GraphvizVisualizer {

    data class Element(val parentId: Int, val parent: ExecutionTreeNode?, val node: ExecutionTreeNode)

    companion object {
        fun drawTree(root: ExecutionTreeNode): String {
            return digraph {

                node {
                    shape = DotNodeShape.RECORD
                    labeljust = "c"
                }

                var count = 0

                val deque = ArrayDeque<Element>().apply {
                    add(Element(count, null, root))
                }

                while (!deque.isEmpty()) {

                    val element = deque.removeFirst()

                    val parentId = element.parentId
                    val parent = element.parent
                    val node = element.node

                    +count.toString()+ {
                        label = "{<f0>${node.expr}|{<f1> σ = ${node.sigma}|<f2> π = ${node.pi}}}"
                    }

                    if (parentId != count) {
                        if (parent != null && parent.expr is Expr.If) {

                            val edgeLabel = if (node == parent.children[0]) {
                                parent.expr.cond
                            } else {
                                when (parent.expr.cond) {
                                    is Expr.Comparison -> {
                                        parent.expr.cond.negate()
                                    }
                                    else -> {
                                        parent.expr.cond
                                    }
                                }
                            }.toString()

                            +"$parentId -> $count"+ {
                                label = edgeLabel
                            }
                        }
                        else {
                            +"$parentId -> $count"+ { }
                        }
                    }

                    // Insert children into dequeue
                    node.children.forEach {
                        deque.add(Element(count, node, it))
                    }

                    count += 1
                }

            }.dot()
        }
    }

}