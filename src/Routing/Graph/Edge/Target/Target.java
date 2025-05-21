package Routing.Graph.Edge.Target;

import Routing.Graph.Graph;
import Routing.Graph.Vertex;

public interface Target {
    Vertex getVertex(Graph g);
}
