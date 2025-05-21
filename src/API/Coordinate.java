package API;

import Routing.Point;

public class Coordinate implements Point {
    private final double lat;
    private final double lon;

    public Coordinate(double lon, double lat) {
        this.lat = lat;
        this.lon = lon;
    }

    @Override
    public double getLat() {
        return lat;
    }

    @Override
    public double getLon() {
        return lon;
    }
}
