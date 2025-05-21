package Routing;

public class RoutingConfig {
    private double walkSpeedMS = 1.4;

    public RoutingConfig() {

    }

    public double getWalkSpeed() {
        return walkSpeedMS;
    }

    public void setWalkSpeed(double walkSpeedMS) {
        if (walkSpeedMS <= 0)
            return;
        this.walkSpeedMS = walkSpeedMS;
    }
}
