package Routing.Graph.generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import API.Coordinate;
import Routing.Point;
import Routing.Graph.Graph;
import Routing.Graph.Vertex;
import Routing.Graph.Edge.BusEdge;
import Routing.Graph.Edge.Edge;
import Routing.Graph.Edge.Target.VertexTarget;
import SQL.DatabaseController;
import SQL.StopNode;
import SQL.StopsGraph;

public class BusGraphBuilder extends Generator {
    public BusGraphBuilder(Graph g) {
        super(g);
    }

    public Graph build() {
        HashMap<String, Vertex> stopsHashMap = new HashMap<>();
        List<Vertex> stopsList = new ArrayList<>();

        StopsGraph stopsGraph = DatabaseController.createGraphNoWalkingEdges();

        for (StopNode stopNode : stopsGraph.getNodesList()) {
            Vertex stop = createStopVertex(new Coordinate(stopNode.getLon(), stopNode.getLat()));
            stopsList.add(stop);
            stopsHashMap.put(stopNode.getStopId(), stop);
        }

        for (StopNode stopNode : stopsGraph.getNodesList()) {
            Vertex thisStopVertex = stopsHashMap.get(stopNode.getStopId());
            for (SQL.Edge edge : stopNode.getOutgoingEdgesAsList()) {
                Vertex nextStopVertex = stopsHashMap.get(edge.getDestinationStopId());
                BusEdge busEdge = new BusEdge(new VertexTarget(nextStopVertex), edge.getOriginStopId(), edge.getDestinationStopId());

                for (SQL.TimeSlot timeSlot : edge.getTimeSlots()) {
                    busEdge.addDeparture(timeSlot.tripId(), timeSlot.startTime().toLocalTime(),
                            timeSlot.endTime().toLocalTime());
                }

                thisStopVertex.getEdges().add(busEdge);
            }
        }

        for (Vertex vertex : stopsList)
            linkStopVertexToRegularVertex(vertex);

        for (Vertex stopVertex : stopsList)
            g.addVertex(stopVertex);

        return g;
    }

    /**
     * @return a vertex object with the exact same coordinates as the stop
     */
    private Vertex createStopVertex(Point x) {
        return new Vertex(x.getLat(), x.getLon());
    }

    /**
     * to be able to leave a bus stop and change transport mode, or enter the bus
     * network
     * Create a bidirectional connection between a nearby vertex on the graph and
     * the new stop
     * to allow entering or exiting the bus network and changing transport modes at
     * the bus stop.
     */
    private void linkStopVertexToRegularVertex(Vertex stop) {
        Vertex vN = g.approximateVertex(stop);
        stop.getEdges().add(new Edge(new VertexTarget(vN), 0));
        vN.getEdges().add(new Edge(new VertexTarget(stop), 0));
    }
}
