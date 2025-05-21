package API;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class ApiRequestLimiter {
    public static ApiRequestsLog apiRequestsLog = ApiRequestsLogManager.getRequestsLog();

    private static void updateRequestsLog() {

        assert apiRequestsLog != null;
        LocalDateTime lastAccessTime = apiRequestsLog.getLastAccessTime();

        LocalDateTime currentTime = LocalDateTime.now();
        apiRequestsLog.setLastAccessTime(currentTime); // update the time on the report Log to the current time

        long secondsPassed = lastAccessTime.until(currentTime, java.time.temporal.ChronoUnit.SECONDS);

        long minutesPassed = lastAccessTime.until(currentTime, java.time.temporal.ChronoUnit.MINUTES);

        long hoursPassed = lastAccessTime.until(currentTime, java.time.temporal.ChronoUnit.HOURS);

        long daysPassed = lastAccessTime.until(currentTime, ChronoUnit.DAYS);

        if (secondsPassed < 5.0) {
            apiRequestsLog.setRequestsTheseFiveSeconds(apiRequestsLog.getRequestsTheseFiveSeconds() + 1);
        } else {
            apiRequestsLog.setRequestsTheseFiveSeconds(0);

            if (minutesPassed < 1.0) {
                apiRequestsLog.setRequestsThisMinute(apiRequestsLog.getRequestsThisMinute() + 1);
            } else {
                apiRequestsLog.setRequestsThisMinute(0);

                if (hoursPassed < 1.0) {
                    apiRequestsLog.setRequestsThisHour(apiRequestsLog.getRequestsThisHour() + 1);
                } else {
                    apiRequestsLog.setRequestsThisHour(0);

                    if (daysPassed < 1.0) {
                        apiRequestsLog.setRequestsThisDay(apiRequestsLog.getRequestsThisDay() + 1);
                    } else {
                        apiRequestsLog.setRequestsThisDay(0);
                    }
                }
            }
        }
    }

    public static boolean authorize() {
        updateRequestsLog();
        return apiRequestsLog.getRequestsTheseFiveSeconds() <= 1
                && apiRequestsLog.getRequestsThisMinute() <= 5
                && apiRequestsLog.getRequestsThisHour() <= 40
                && apiRequestsLog.getRequestsThisDay() <= 100;
    }
}
