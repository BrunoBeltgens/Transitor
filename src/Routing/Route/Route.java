package Routing.Route;

import Routing.Point;
import Routing.Graph.algorithms.Visitor;
import SQL.Trip;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Route {
    private final List<Transfer> transfers = new ArrayList<>();
    private Duration duration;
    private int distance;
    private final LocalDateTime eta;
    private final LocalDateTime startDateTime;
    
    private Trip trip;
    private List<Point> points;

    public Route(List<Visitor> visitors, LocalDateTime startDateTime, Duration duration, LocalDateTime eta, int distance) {
        this.duration = duration;
        this.startDateTime = startDateTime;
        this.eta = eta;
        this.distance = distance / 100;

        List<Visitor> group = null;
        Visitor firstInTransfer = null;
        for (int i = 1; i < visitors.size(); i++) {
            Visitor vis = visitors.get(i);
            if (firstInTransfer == null || vis.isTransfer(firstInTransfer)) {
                firstInTransfer = vis;
                if (group != null)
                    transfers.add(new Transfer(group));
                group = new ArrayList<>();
                if (vis.getConnection() != null)
                    group.add(visitors.get(i - 1));
            }
            group.add(vis);
        }
        if (group != null)
            transfers.add(new Transfer(group));
    }

    public Route(List<Point> points, LocalDateTime startDateTime, LocalDateTime eta, Trip trip) {
        this.startDateTime = startDateTime;
        this.eta = eta;
        this.trip = trip;

        this.points = points;
    }

    public Trip getTrip() {
        return trip;
    }

    public List<Point> getPoints() {
        return points;
    }

    public void addWeight(int weight) {
        distance += weight;
    }

    public List<Transfer> getTransfers() {
        return transfers;
    }

    public int getDistance() {
        return distance;
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startDateTime;
    }

    public LocalDateTime getETA() {
        return eta;
    }
}
