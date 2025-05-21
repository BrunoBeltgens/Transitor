package Routing.Graph;

import java.util.ArrayList;
import java.util.List;

import Routing.Point;
import Routing.Graph.Edge.Edge;

public class Vertex implements Point {
    private final double latitude;
    private final double longitude;
    private final List<Edge> edges;

    public Vertex(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.edges = new ArrayList<>();
    }

    public List<Edge> getEdges() {
        return edges;
    }

    @Override
    public double getLat() {
        return latitude;
    }

    @Override
    public double getLon() {
        return longitude;
    }
}
