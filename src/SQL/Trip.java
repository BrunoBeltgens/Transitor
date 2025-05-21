package SQL;

import java.sql.Time;
import java.util.List;
import java.util.Comparator;

public class Trip {
    private final Stop startStop;
    private List<TripNode> tripNodesList; // contains the stopId and tripId at every bus stop in the trip (tripID varies with transfers)
    private final Stop endStop;
    private final Time startTime;
    private final Time endTime;
    private final int tripID;

    public Trip(Stop startStop, Stop endStop, Time startTime, Time endTime, int tripID) {
        this.startStop = startStop;
        this.endStop = endStop;
        this.startTime = startTime;
        this.endTime = endTime;
        this.tripID = tripID;
    }

    public void setTripNodesInTrip(List<TripNode> tripNodesList) {
        this.tripNodesList = tripNodesList;
    }

    public Stop getStartStop() {
        return startStop;
    }

    public List<TripNode> getTripNodesList() {
        return tripNodesList;
    }

    public Stop getEndStop() {
        return endStop;
    }

    public Time getStartTime() {
        return startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public int getTripID() {
        return tripID;
    }

    public static class DistanceComparator implements Comparator<Trip> {
        @Override
        public int compare(Trip t1, Trip t2) {
            double totalDistance1 = t1.getStartStop().getDistanceFromPoint() + t1.getEndStop().getDistanceFromPoint();
            double totalDistance2 = t2.getStartStop().getDistanceFromPoint() + t2.getEndStop().getDistanceFromPoint();
            return Double.compare(totalDistance1, totalDistance2);
        }
    }
}
