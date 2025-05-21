package Routing.Graph.Edge;

import java.time.Duration;

import Routing.RoutingConfig;
import Routing.Route.Connection;
import Routing.Graph.Graph;
import Routing.Graph.Vertex;
import Routing.Graph.Edge.Target.Target;
import Routing.Graph.Transport.Walking;
import Routing.Graph.algorithms.Visitor;

public class Edge {
    private final Target target;
    protected int weight;

    public Edge(Target target, int weight) {
        this.target = target;
        this.weight = weight;
    }

    public Vertex getTargetVertex(Graph g) {
        return target.getVertex(g);
    }

    public int getWeight() {
        return weight;
    }

    public Connection createConnection(Visitor prev, RoutingConfig config) {
        double averageWalkingSpeed = config.getWalkSpeed();
        double distanceInMeters = weight / 100.0;
        double travelTimeInSeconds = distanceInMeters / averageWalkingSpeed;
        Duration travelTime = Duration.ofSeconds((long) travelTimeInSeconds);

        return new Connection(travelTime, weight, new Walking(), false, false);
    }
}
