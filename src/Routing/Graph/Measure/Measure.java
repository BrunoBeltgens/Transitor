package Routing.Graph.Measure;

import Routing.Graph.algorithms.Visitor;

public interface Measure {
    boolean less(Visitor a, Visitor b);
    int compare(Visitor a, Visitor b);
}
