package Routing.Graph.Transport;

public class Walking implements Transport {
    @Override
    public String getTransportType() {
        return "WALK";
    }

    @Override
    public boolean isTransfer(Transport transport) {
        return !(transport instanceof Walking);
    }

    @Override
    public Transport mergeTransport(Transport transport) {
        return this;
    }
}
