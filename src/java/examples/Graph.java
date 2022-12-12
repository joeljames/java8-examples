package examples;

import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import com.google.common.graph.Traverser;

import java.util.Iterator;

public class Graph {

    public static void main(String[] args) {
        MutableGraph<Integer> mutableGraph = GraphBuilder
                .directed()
                .allowsSelfLoops(true) //A node can point to itself e.g., mutableGraph.putEdge(3, 3)
                .build();

        mutableGraph.addNode(0);
        mutableGraph.addNode(1);
        mutableGraph.addNode(2);
        mutableGraph.addNode(3);

        mutableGraph.putEdge(0, 1);
        mutableGraph.putEdge(0, 2);
        mutableGraph.putEdge(1, 2);
        mutableGraph.putEdge(2, 0);
        mutableGraph.putEdge(2, 3);
        mutableGraph.putEdge(3, 3);

        Traverser<Integer> traversedGraph = Traverser.forGraph(mutableGraph);
        Iterator<Integer> breadthFirstIterator = traversedGraph.breadthFirst(2).iterator();

        //Graph {0: [1, 2], 1: [2], 2: [0, 3], 3: [3]})
        //A Breadth First Traversal of the following graph is 2, 0, 3, 1.
        System.out.println("Following is Breadth First Traversal (starting from vertex 2).");
        while (breadthFirstIterator.hasNext()) {
            Integer node = breadthFirstIterator.next();
            System.out.println(node);
        }
    }
}
