package SQL;

import java.sql.Time;

public record TimeSlot(int tripId, Time startTime, Time endTime) {
}
