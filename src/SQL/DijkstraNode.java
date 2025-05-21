package SQL;

import java.sql.Time;

public class DijkstraNode {
    private final StopNode stopNode;
    private final Time time;

    public DijkstraNode(StopNode stopNode, Time time) {
        this.stopNode = stopNode;
        this.time = time;
    }

    public StopNode getStopNode() {
        return stopNode;
    }

    public Time getTime() {
        return time;
    }
}
