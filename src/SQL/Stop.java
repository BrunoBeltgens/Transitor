package SQL;

public class Stop {
    private final String id;
    private final double lat;
    private final double lon;
    private final String stop_name;
    private double distanceFromPoint;

    public Stop(String id, double lat, double lon, String stop_name) {

        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.stop_name = stop_name;
    }

    public void setDistanceFromPoint(double distanceFromPoint) {
        this.distanceFromPoint = distanceFromPoint;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getStop_name() {
        return stop_name;
    }

    public double getDistanceFromPoint() {
        return distanceFromPoint;
    }

    public String getId() {
        return id;
    }
}
