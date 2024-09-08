package net.minebr.object;

public class Booster {
    private final String type;
    private final double percentage;
    private final long remainingTime;

    public Booster(String type, double percentage, long remainingTime) {
        this.type = type;
        this.percentage = percentage;
        this.remainingTime = remainingTime;
    }

    public String getType() {
        return type;
    }

    public double getPercentage() {
        return percentage;
    }

    public long getRemainingTime() {
        return remainingTime;
    }
}
