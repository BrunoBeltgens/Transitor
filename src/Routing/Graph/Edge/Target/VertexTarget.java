package Routing.Graph.Edge.Target;

import Routing.Graph.Graph;
import Routing.Graph.Vertex;

public class VertexTarget implements Target {
    private final Vertex t;

    @Override
    public Vertex getVertex(Graph g) {
        return t;
    }

    public VertexTarget(Vertex target) {
        t = target;
    }
}
