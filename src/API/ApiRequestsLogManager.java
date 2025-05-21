package API;

import java.io.*;
import java.time.LocalDateTime;

public class ApiRequestsLogManager {
    private static final String requestsLogFilePath = "resources/requestsLog.txt";

    public static void writeRequestsLog(ApiRequestsLog apiRequestsLog) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(requestsLogFilePath))) {
            oos.writeObject(apiRequestsLog);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ApiRequestsLog getRequestsLog() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(requestsLogFilePath))) {
            return (ApiRequestsLog) ois.readObject();
        } catch (FileNotFoundException | EOFException ignored) {
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) {
        ApiRequestsLog apiRequestsLog = new ApiRequestsLog();
        apiRequestsLog.setRequestsThisDay(0);
        apiRequestsLog.setRequestsThisHour(0);
        apiRequestsLog.setRequestsThisMinute(0);
        apiRequestsLog.setRequestsTheseFiveSeconds(0);
        apiRequestsLog.setLastAccessTime(LocalDateTime.now());

        writeRequestsLog(apiRequestsLog);
    }
}
