package Routing.Graph.Transport;

import java.time.Duration;
import java.time.LocalTime;

public class Bus implements Transport {
    private final int trip_id;
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final Duration waitTime;
    private final String stopIdOrigin;
    private final String stopIdDestination;

    public Bus(int trip_id, String stopIdOrigin, String stopIdDestination, LocalTime startTime, LocalTime endTime, Duration waitTime) {
        this.trip_id = trip_id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.waitTime = waitTime;
        this.stopIdOrigin = stopIdOrigin;
        this.stopIdDestination = stopIdDestination;
    }

    @Override
    public String getTransportType() {
        return "BUS";
    }

    public Duration getWaitTime() {
        return waitTime;
    }

    public int getTripId() {
        return trip_id;
    }

    public String getOriginStopID() {
        return stopIdOrigin;
    }

    public String getDestinationStopID() {
        return stopIdDestination;
    }

    public LocalTime getDepartureTime() {
        return startTime;
    }

    public LocalTime getArrivalTime() {
        return endTime;
    }

    @Override
    public boolean isTransfer(Transport transport) {
        return !(transport instanceof Bus) || trip_id != ((Bus) transport).getTripId();
    }

    @Override
    public Transport mergeTransport(Transport transport) {
        return new Bus(trip_id, stopIdOrigin, ((Bus) transport).stopIdDestination, startTime, ((Bus) transport).endTime, waitTime);
    }
}
