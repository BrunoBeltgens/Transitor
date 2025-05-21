package Routing;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import Routing.Route.Route;
import Routing.Route.Transfer;
import Routing.Graph.Graph;
import Routing.Graph.Vertex;
import Routing.Graph.Measure.ETA;
import Routing.Graph.Measure.Measure;
import Routing.Graph.generator.BusGraphBuilder;
import Routing.Graph.generator.JSONGraph;

public class RoutingEngine {
    private final Graph g;
    private final Graph g_with_busses;
    private RoutingConfig config = new RoutingConfig();

    public RoutingEngine() {
        final String path = "resources/maastrichtGraph.json";
        
        System.out.println("[RoutingEngine] Building graph");
        g = new JSONGraph(new Graph(), path).build();
        System.out.println("[RoutingEngine] Successfully built graph");

        System.out.println("[RoutingEngine] Building graph-gtfs");
        g_with_busses = new BusGraphBuilder(new JSONGraph(new Graph(), path).build()).build();
        System.out.println("[RoutingEngine] Successfully built graph-gtfs");
    }

    public Route route(Graph graph, LocalDateTime dateTime, Point a, Point b, Measure mode) {
        Vertex vS = graph.approximateVertex(a);
        Vertex vE = graph.approximateVertex(b);
        return graph.shortestPath(config, dateTime, vS, vE, mode);
    }

    public Route routeWalking(LocalDateTime dateTime, Point a, Point b) {
        return route(g, dateTime, a, b, new ETA()); // Speed is assumed to be the same everywhere so the fastest route = shortest route
    }

    public Route routePublicTransport(LocalDateTime dateTime, Point a, Point b) {
        // return SQL.DatabaseController.getBestRoute(a, b, dateTime.getHour(), dateTime.getMinute(), dateTime.getSecond());

        Route route = route(g_with_busses, dateTime, a, b, new ETA());
        for (Transfer tf : route.getTransfers()) {
            if (tf.getConnection().requiresInterpolation()) {
                List<Point> points = tf.getPoints();
                List<Point> buffer = new ArrayList<>();
                for (int i = 0; i < points.size() - 1; i++) {
                    Route subRoute = routeWalking(dateTime, points.get(i), points.get(i + 1));
                    if (tf.getConnection().requiresDistance())
                        route.addWeight(subRoute.getDistance());
                    for (Transfer subTf : subRoute.getTransfers())
                        buffer.addAll(subTf.getPoints());
                }
                buffer.add(0, points.get(0));
                buffer.add(points.get(points.size() - 1));
                points.clear();
                points.addAll(buffer);
            }
        }
        return route;
    }

    public void setConfig(RoutingConfig configuration) {
        config = configuration;
    }
}
