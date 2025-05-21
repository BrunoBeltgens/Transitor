package App;

public class TimeCalculator {
    private final static double averageBikeSpeed = 18;

    public static double calculateBikeTime(double distance) {
        double time = distance / averageBikeSpeed * 60;
        if (time > 60) {
            time = time / 60;
            return time;
        }
        return time;
    }
}
