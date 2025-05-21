package Routing.Graph.Edge;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;

import Routing.RoutingConfig;
import Routing.Route.Connection;
import Routing.Graph.Edge.Target.Target;
import Routing.Graph.Transport.Bus;
import Routing.Graph.algorithms.Visitor;

public class BusEdge extends Edge {
    private final ArrayList<Departure> departures = new ArrayList<>();
    private final String stopIdOrigin;
    private final String stopIdDestination;

    private record Departure(int trip_id, LocalTime startTime, LocalTime endTime) {
    }
    
    public BusEdge(Target target, String stopIdOrigin, String stopIdDestination) {
        super(target, 0);
        this.stopIdOrigin = stopIdOrigin;
        this.stopIdDestination = stopIdDestination;
    }

    @Override
    public Connection createConnection(Visitor prev, RoutingConfig config) {
        LocalTime currentTime = prev.getArrivalTime().toLocalTime();

        Duration bestDuration = null;
        Duration bestWaitTime = null;

        Departure selectedDeparture = null;
        for (Departure departure : departures) {
            Duration tripDuration = calculateTripDuration(departure.startTime, departure.endTime);
            Duration tripWaitTime = calculateWaitTime(currentTime, departure.startTime);

            Duration totalDuration = tripDuration.plus(tripWaitTime);
            if (selectedDeparture == null || totalDuration.compareTo(bestDuration) < 0) {
                selectedDeparture = departure;
                bestDuration = totalDuration;
                bestWaitTime = tripWaitTime;
            }
        }

        if (selectedDeparture == null) {
            throw new IllegalStateException("No valid departure found for the current time: " + currentTime);
        }

        return new Connection(bestDuration, 0,
                new Bus(selectedDeparture.trip_id, stopIdOrigin, stopIdDestination, selectedDeparture.startTime, selectedDeparture.endTime, bestWaitTime),
                true, true);
    }

    public void addDeparture(int trip_id, LocalTime startTime, LocalTime endTime) {
        departures.add(new Departure(trip_id, startTime, endTime));
    }

    private Duration calculateTripDuration(LocalTime start, LocalTime end) {
        if (end.isBefore(start)) {
            // Crosses midnight
            return Duration.between(start, LocalTime.MAX).plus(Duration.between(LocalTime.MIN, end));
        } else {
            return Duration.between(start, end);
        }
    }

    private Duration calculateWaitTime(LocalTime current, LocalTime departure) {
        if (departure.isBefore(current) || departure.equals(current)) {
            // Departure is on the next day
            return Duration.between(current, LocalTime.MAX).plus(Duration.between(LocalTime.MIN, departure));
        } else {
            return Duration.between(current, departure);
        }
    }
}
