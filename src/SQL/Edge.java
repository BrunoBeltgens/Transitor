package SQL;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Edge {
    private final String originStopId;
    private final String destinationStopId;

    private final List<TimeSlot> timeSlots = new ArrayList<>();

    public Edge(String originStopId, String destinationStopId) {
        this.originStopId = originStopId;
        this.destinationStopId = destinationStopId;
    }

    public String getOriginStopId() {
        return originStopId;
    }

    public String getDestinationStopId() {
        return destinationStopId;
    }

    public List<TimeSlot> getTimeSlots() {
        return timeSlots;
    }

    public void addTimeSlot(int tripId, Time startTime, Time endTime) {
        timeSlots.add(new TimeSlot(tripId, startTime, endTime));
    }

    public TimeSlot getEarliestTimeSlot(Time startingTime) {
        sortTimeSlots();

        for (TimeSlot timeSlot : timeSlots) {
            if (!timeSlot.startTime().before(startingTime)) // !before includes the case when the time is the same, as well as later
                return timeSlot;
        }

        if (!timeSlots.isEmpty())
            return timeSlots.get(0);

        return null;
    }

    public void sortTimeSlots() {
        timeSlots.sort(Comparator.comparing(TimeSlot::startTime));
    }
}
