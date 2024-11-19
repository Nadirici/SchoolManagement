package fr.cyu.schoolmanagementsystem.util;

import java.util.ArrayList;
import java.util.List;

public class CompositeStats {
    private double average;
    private double min;
    private double max;
    private int count;

    private final List<CompositeStats> components;

    public CompositeStats() {
        components = new ArrayList<>();
    }

    public void addComponent(CompositeStats component) {
        components.add(component);
        recalculateStats();
    }

    private void recalculateStats() {
        double totalAverage = 0;
        double totalMin = Double.MAX_VALUE;
        double totalMax = Double.MIN_VALUE;

        count = 0;
        for (CompositeStats component : components) {
            totalAverage += component.getAverage();
            totalMin = Math.min(totalMin, component.getMin());
            totalMax = Math.max(totalMax, component.getMax());
            count++;
        }

        this.average = count > 0 ? totalAverage / count : 0;
        this.min = totalMin == Double.MAX_VALUE ? 0 : totalMin;
        this.max = totalMax == Double.MIN_VALUE ? 0 : totalMax;
    }

    public double getAverage() {
        return average;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public int getCount() {
        return count;
    }
}
