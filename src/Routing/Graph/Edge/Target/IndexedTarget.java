package Routing.Graph.Edge.Target;

import Routing.Graph.Graph;
import Routing.Graph.Vertex;

public class IndexedTarget implements Target {
    private final int i;

    @Override
    public Vertex getVertex(Graph g) {
        return g.getVertex(i);
    }

    public IndexedTarget(int index) {
        i = index;
    }
}
