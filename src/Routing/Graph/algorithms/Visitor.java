package Routing.Graph.algorithms;

import java.time.LocalDateTime;

import Routing.RoutingConfig;
import Routing.Route.Connection;
import Routing.Graph.Vertex;
import Routing.Graph.Edge.Edge;

public class Visitor {
    private final LocalDateTime arrivalTime;
    private final int cumulativeDistance;
    private final Vertex vertex;
    private Visitor prev;

    private Connection predecessorConnection;

    public Visitor(Vertex location, LocalDateTime time) {
        vertex = location;
        arrivalTime = time;
        cumulativeDistance = 0;
    }

    public Visitor(Vertex location, Edge edge, RoutingConfig config, Visitor prevVisitor) {
        vertex = location;
        prev = prevVisitor;
        predecessorConnection = edge.createConnection(prev, config);
        arrivalTime = prev.getArrivalTime().plus(predecessorConnection.getTime());
        cumulativeDistance = prevVisitor.cumulativeDistance + predecessorConnection.getDistance();
    }

    public boolean isAt(Vertex location) {
        return location == vertex;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public int getDistance() {
        return cumulativeDistance;
    }

    public Vertex getVertex() {
        return vertex;
    }

    public Visitor getPrev() {
        return prev;
    }

    public Connection getConnection() {
        return predecessorConnection;
    }

    public boolean isTransfer(Visitor vis) {
        return predecessorConnection.isTransfer(vis.getConnection());
    }
}
