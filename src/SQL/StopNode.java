package SQL;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class StopNode {
    private final String stopId;
    private final double lat;
    private final double lon;
    private final HashMap<String, Edge> outgoingEdges; // Key is the stopId of the other end of the edge

    public StopNode(String stopId, double lat, double lon) {
        this.stopId = stopId;
        outgoingEdges = new HashMap<>();
        this.lat = lat;
        this.lon = lon;
    }

    public String getStopId() {
        return stopId;
    }

    public void addEdgeTowards(String destinationStopId) {
        outgoingEdges.put(destinationStopId, new Edge(this.stopId, destinationStopId));
    }

    public boolean hasEdgeTo(String stopId) {
        return outgoingEdges.containsKey(stopId);
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public Edge getEdgeTo(String stopId) {
        if (outgoingEdges.containsKey(stopId)) {
            return outgoingEdges.get(stopId);
        }
        return null;
    }

    public List<Edge> getOutgoingEdgesAsList() {
        Collection<Edge> values = outgoingEdges.values();
        return new ArrayList<>(values);
    }

    public boolean equals(StopNode stopNode) {
        return this.stopId.equals(stopNode.getStopId());
    }
}
