package Routing.testing;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Random;

import Routing.RoutingConfig;
import Routing.Route.Route;
import Routing.Route.Transfer;
import Routing.Graph.Graph;
import Routing.Graph.Vertex;
import Routing.Graph.Edge.BusEdge;
import Routing.Graph.Edge.Edge;
import Routing.Graph.Edge.Target.IndexedTarget;
import Routing.Graph.Measure.ETA;
import Routing.Graph.Transport.Bus;
import Routing.Graph.algorithms.Visitor;
import Routing.Graph.generator.JSONGraph;

public class GraphTesting {
    public static void main(String[] args) {
        GraphTesting gt = new GraphTesting();
        gt.testSmall();
        gt.testBigRandom();
    }

    public void testSmall() {
        Graph g = new Graph();

        Vertex v = new Vertex(0, 0);
        v.getEdges().add(new Edge(new IndexedTarget(1), 4));
        v.getEdges().add(new Edge(new IndexedTarget(7), 8));
        g.addVertex(v);

        Vertex v1 = new Vertex(0, 0);
        v1.getEdges().add(new Edge(new IndexedTarget(2), 8));
        v1.getEdges().add(new Edge(new IndexedTarget(7), 11));
        v1.getEdges().add(new Edge(new IndexedTarget(0), 4));
        g.addVertex(v1);

        Vertex v2 = new Vertex(0, 0);
        v2.getEdges().add(new Edge(new IndexedTarget(3), 7));
        v2.getEdges().add(new Edge(new IndexedTarget(5), 4));
        v2.getEdges().add(new Edge(new IndexedTarget(1), 8));
        v2.getEdges().add(new Edge(new IndexedTarget(8), 2));
        g.addVertex(v2);

        Vertex v3 = new Vertex(0, 0);
        v3.getEdges().add(new Edge(new IndexedTarget(4), 9));
        v3.getEdges().add(new Edge(new IndexedTarget(5), 14));
        v3.getEdges().add(new Edge(new IndexedTarget(2), 7));
        g.addVertex(v3);

        Vertex v4 = new Vertex(0, 0);
        v4.getEdges().add(new Edge(new IndexedTarget(3), 9));
        v4.getEdges().add(new Edge(new IndexedTarget(5), 10));
        g.addVertex(v4);

        Vertex v5 = new Vertex(0, 0);
        v5.getEdges().add(new Edge(new IndexedTarget(3), 14));
        v5.getEdges().add(new Edge(new IndexedTarget(4), 10));
        v5.getEdges().add(new Edge(new IndexedTarget(2), 4));
        v5.getEdges().add(new Edge(new IndexedTarget(6), 2));
        g.addVertex(v5);

        Vertex v6 = new Vertex(0, 0);
        v6.getEdges().add(new Edge(new IndexedTarget(5), 2));
        v6.getEdges().add(new Edge(new IndexedTarget(8), 6));
        v6.getEdges().add(new Edge(new IndexedTarget(7), 1));
        g.addVertex(v6);

        Vertex v7 = new Vertex(0, 0);
        v7.getEdges().add(new Edge(new IndexedTarget(6), 1));
        v7.getEdges().add(new Edge(new IndexedTarget(8), 7));
        v7.getEdges().add(new Edge(new IndexedTarget(0), 8));
        v7.getEdges().add(new Edge(new IndexedTarget(1), 11));
        g.addVertex(v7);

        Vertex v8 = new Vertex(0, 0);
        v8.getEdges().add(new Edge(new IndexedTarget(2), 2));
        v8.getEdges().add(new Edge(new IndexedTarget(6), 6));
        v8.getEdges().add(new Edge(new IndexedTarget(7), 7));
        g.addVertex(v8);

        BusEdge busEdge = new BusEdge(new IndexedTarget(4), "", "");
        busEdge.addDeparture(1001, LocalTime.of(0, 0, 9), LocalTime.of(0, 0, 9));
        busEdge.addDeparture(1002, LocalTime.of(0, 0, 10), LocalTime.of(0, 0, 11));
        busEdge.addDeparture(1003, LocalTime.of(0, 0, 11), LocalTime.of(0, 0, 12));

        v5.getEdges().add(busEdge);

        BusEdge busEdge2 = new BusEdge(new IndexedTarget(7), "", "");
        busEdge2.addDeparture(1001, LocalTime.of(0, 0, 0), LocalTime.of(0, 0, 1));

        v.getEdges().add(busEdge2);

        Vertex v9 = new Vertex(0, 0);
        g.addVertex(v9);

        BusEdge busEdge3 = new BusEdge(new IndexedTarget(9), "", "");
        busEdge3.addDeparture(1001, LocalTime.of(0, 0, 9), LocalTime.of(0, 0, 20));
        v4.getEdges().add(busEdge3);

        Route route = g.shortestPath(new RoutingConfig(), LocalDateTime.now(), v, v9, new ETA());

        for (Transfer transfer : route.getTransfers()) {
            System.out.print("Transfer vertices: ");
            for (Visitor visitor : transfer.getVisitors()) {
                for (int i = 0; i < 9; i++)
                    if (g.getVertex(i) == visitor.getVertex())
                        System.out.print(i + " ");
            }
            String type = transfer.getTransport().getTransportType();
            System.out.print(", from " + transfer.getStartDateTime() + " to " + transfer.getEndDateTime() + ", by "
                    + type + ", info: ");
            if (type.equals("BUS")) {
                Bus bus = (Bus) transfer.getTransport();
                System.out.print("tripID: " + bus.getTripId() + ", wait time: " + bus.getWaitTime().toSeconds());
            }
            System.out.println();
        }

        System.out.println("distance: " + route.getDistance() + ", dur: " + route.getDuration().getSeconds() + ", eta: "
                + route.getETA());
    }

    public void testBigRandom() {
        Graph g = new JSONGraph(new Graph(), "resources/maastrichtGraph.json").build();
        Random rand = new Random();
        g.shortestPath(new RoutingConfig(), LocalDateTime.now(), g.getVertex(rand.nextInt(g.v() - 1)), g.getVertex(rand.nextInt(g.v() - 1)), new ETA());
    }
}
