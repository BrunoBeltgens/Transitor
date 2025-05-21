package Routing.Graph;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import Routing.Point;
import Routing.RoutingConfig;
import Routing.Route.Route;
import Routing.Graph.Measure.Measure;
import Routing.Graph.algorithms.Dijkstra;
import Routing.Graph.algorithms.SpatialIndex;

public class Graph {
    private final List<Vertex> vertices = new ArrayList<>();
    private final SpatialIndex spatialIndex = new SpatialIndex();

    public Graph() {
    }

    public Vertex getVertex(int index) {
        return vertices.get(index);
    }

    public void addVertex(Vertex v) {
        spatialIndex.insert(v);
        vertices.add(v);
    }

    public Route shortestPath(RoutingConfig config, LocalDateTime startTime, Vertex start, Vertex end, Measure mode) {
        Dijkstra sp = new Dijkstra(this, config, startTime, start, end, mode);
        return sp.getRoute();
    }

    public int v() {
        return vertices.size();
    }

    public Vertex approximateVertex(Point target) {
        return spatialIndex.nearest(target);
    }
}