package Routing.Graph.Transport;

public interface Transport {
    String getTransportType();
    boolean isTransfer(Transport transport);
    Transport mergeTransport(Transport transport);
}