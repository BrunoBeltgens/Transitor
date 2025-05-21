package Routing.Graph.generator;

import Routing.Graph.Graph;

public abstract class Generator {
    protected final Graph g;
    public abstract Graph build();

    public Generator(Graph g) {
        this.g = g;
    }
}
