package API;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ApiRequestsLog implements Serializable {
    private LocalDateTime lastAccessTime;
    private int requestsTheseFiveSeconds = 0;
    private int requestsThisMinute = 0;
    private int requestsThisHour = 0;
    private int requestsThisDay = 0;

    public int getRequestsTheseFiveSeconds() {
        return requestsTheseFiveSeconds;
    }

    public int getRequestsThisMinute() {
        return requestsThisMinute;
    }

    public int getRequestsThisHour() {
        return requestsThisHour;
    }

    public int getRequestsThisDay() {
        return requestsThisDay;
    }

    public LocalDateTime getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(LocalDateTime lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public void setRequestsTheseFiveSeconds(int requestsTheseFiveSeconds) {
        this.requestsTheseFiveSeconds = requestsTheseFiveSeconds;
    }

    public void setRequestsThisMinute(int requestsThisMinute) {
        this.requestsThisMinute = requestsThisMinute;
    }

    public void setRequestsThisHour(int requestsThisHour) {
        this.requestsThisHour = requestsThisHour;
    }

    public void setRequestsThisDay(int requestsThisDay) {
        this.requestsThisDay = requestsThisDay;
    }
}
