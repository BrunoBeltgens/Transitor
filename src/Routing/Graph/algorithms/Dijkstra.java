package Routing.Graph.algorithms;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import Routing.RoutingConfig;
import Routing.Route.Route;
import Routing.Graph.Graph;
import Routing.Graph.Vertex;
import Routing.Graph.Edge.Edge;
import Routing.Graph.Measure.Measure;

public class Dijkstra {
    private final Route route;

    public Dijkstra(Graph g, RoutingConfig config, LocalDateTime startTime, Vertex start, Vertex end, Measure mode) {
        HashMap<Vertex, Visitor> visitors = new HashMap<>();
        Comparator<Visitor> visitorComparator = mode::compare;
        Queue<Visitor> pq = new PriorityQueue<>(visitorComparator);
        Visitor startVisitor = new Visitor(start, startTime);
        pq.add(startVisitor);

        while (!pq.isEmpty()) {
            Visitor v = pq.poll();
            if (v.isAt(end))
                break;

            for (Edge e : v.getVertex().getEdges()) {
                Vertex vT = e.getTargetVertex(g);
                if (vT == null)
                    continue;
                Visitor t = new Visitor(vT, e, config, v);
                if (!visitors.containsKey(vT) || mode.less(t, visitors.get(vT))) {
                    visitors.put(vT, t);
                    pq.add(t);
                }
            }
        }

        List<Visitor> visitorsList = new ArrayList<>();
        for (Visitor vis = visitors.get(end); vis != null; vis = vis.getPrev())
            visitorsList.add(0, vis);

        LocalDateTime arrivalTime = visitors.get(end) == null ? startTime : visitors.get(end).getArrivalTime();
        Duration duration = visitors.get(end) == null ? Duration.ofSeconds(-1) : Duration.between(startTime, arrivalTime);
        int distance = visitors.get(end) == null ? 0 : visitors.get(end).getDistance();

        route = new Route(visitorsList, startTime, duration, arrivalTime, distance);
    }

    public Route getRoute() {
        return route;
    }
}
