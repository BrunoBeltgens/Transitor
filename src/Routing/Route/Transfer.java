package Routing.Route;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import Routing.Point;
import Routing.Graph.Transport.Transport;
import Routing.Graph.algorithms.Visitor;

public class Transfer {
    private final List<Point> points = new ArrayList<>();
    private final List<Visitor> visitors;
    private Connection connection;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    public Transfer(List<Visitor> visitors) {
        this.visitors = visitors;

        for (Visitor visitor : visitors)
            points.add(visitor.getVertex());
        connection = visitors.get(visitors.size() - 1).getConnection();

        if (visitors.size() > 2)
            connection = connection.mergeConnection(visitors.get(1).getConnection());

        if (!visitors.isEmpty()) {
            startDateTime = visitors.get(0).getArrivalTime();
            endDateTime = visitors.get(visitors.size() - 1).getArrivalTime();
        }
    }

    public List<Visitor> getVisitors() {
        return visitors;
    }

    public Transport getTransport() {
        return connection.getTransport();
    }

    public Connection getConnection() {
        return connection;
    }

    /**
     * Note: If you need the time the transport departs (which may be later)
     * instead, use <code>((Bus) getTransport()).getDepartureTime()</code> for
     * example.
     * 
     * @return the start date and time when arrived at the first location of the
     *         transfer
     */
    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public String getType() {
        return connection.getTransport().getTransportType();
    }

    public List<Point> getPoints() {
        return points;
    }
}
