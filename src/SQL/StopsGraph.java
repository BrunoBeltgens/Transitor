package SQL;

import java.util.ArrayList;
import java.util.HashMap;

public class StopsGraph {
    private final ArrayList<StopNode> nodesList = new ArrayList<>();
    private final HashMap<String, StopNode> nodesHashMap = new HashMap<>(); // the key is the stop ID

    public StopNode getNode(String stopId) {
        return nodesHashMap.get(stopId);
    }

    public void addNode(StopNode stopNode) {
        nodesList.add(stopNode);
        nodesHashMap.put(stopNode.getStopId(), stopNode);
    }

    public boolean hasNode(String stopId) {
        return nodesHashMap.containsKey(stopId);
    }

    public ArrayList<StopNode> getNodesList() {
        return nodesList;
    }
}
