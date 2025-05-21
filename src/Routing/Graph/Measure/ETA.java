package Routing.Graph.Measure;

import Routing.Graph.algorithms.Visitor;

public class ETA implements Measure {
    @Override
    public boolean less(Visitor a, Visitor b) {
        return a.getArrivalTime().isBefore(b.getArrivalTime());
    }

    @Override
    public int compare(Visitor a, Visitor b) {
        return a.getArrivalTime().compareTo(b.getArrivalTime());
    }
}
