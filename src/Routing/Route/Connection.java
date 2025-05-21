package Routing.Route;

import java.time.Duration;

import Routing.Graph.Transport.Transport;

public class Connection {
    private final boolean requirement_subRouteInterpolation;
    private final boolean requirement_distanceCalculation;

    private final Duration time;
    private final int distance;
    private final Transport transport;

    public Connection(Duration time, int distance, Transport transport, boolean req_interpolation, boolean req_distanceCompute) {
        this.time = time;
        this.distance = distance;
        this.transport = transport;
        requirement_subRouteInterpolation = req_interpolation;
        requirement_distanceCalculation = req_distanceCompute;
    }

    public Duration getTime() {
        return time;
    }

    public int getDistance() {
        return distance;
    }

    public Transport getTransport() {
        return transport;
    }

    public boolean isTransfer(Connection connection) {
        return transport.isTransfer(connection.getTransport());
    }

    public boolean requiresInterpolation() {
        return requirement_subRouteInterpolation;
    }

    public boolean requiresDistance() {
        return requirement_distanceCalculation;
    }

    public Connection mergeConnection(Connection connection) {
        return new Connection(time.plus(connection.getTime()),
                distance + connection.getDistance(),
                transport.mergeTransport(connection.getTransport()),
                requirement_subRouteInterpolation,
                requirement_subRouteInterpolation
        );
    }
}
