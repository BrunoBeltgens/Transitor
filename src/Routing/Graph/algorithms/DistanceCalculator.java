package Routing.Graph.algorithms;

import Routing.Point;

public class DistanceCalculator {
    /**
     * @return the haversine distance in meters between x and y
     */
    public double haversine(Point x, Point y) {
        double R = 6371000;
        double phi_1 = Math.toRadians(x.getLat());
        double phi_2 = Math.toRadians(y.getLat());

        double delta_phi = Math.toRadians(y.getLat() - x.getLat());
        double delta_lambda = Math.toRadians(y.getLon() - x.getLon());

        double a = Math.pow(Math.sin(delta_phi / 2.0), 2)
                + Math.cos(phi_1) * Math.cos(phi_2) * Math.pow(Math.sin(delta_lambda / 2.0), 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }
}
