package fr.cyu.schoolmanagementsystem.util;

public class LeafStats extends CompositeStats {
    private final double average;
    private final double min;
    private final double max;

    public LeafStats(double average, double min, double max) {
        super();
        this.average = average;
        this.min = min;
        this.max = max;
    }

    @Override
    public double getAverage() {
        return average;
    }

    @Override
    public double getMin() {
        return min;
    }

    @Override
    public double getMax() {
        return max;
    }
}
