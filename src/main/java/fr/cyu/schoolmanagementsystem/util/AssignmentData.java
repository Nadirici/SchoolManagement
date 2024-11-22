package fr.cyu.schoolmanagementsystem.util;

import fr.cyu.schoolmanagementsystem.entity.Grade;

public class AssignmentData {
    private CompositeStats stats;
    private Grade grade;

    // Constructor
    public AssignmentData(CompositeStats stats, Grade grade) {
        this.stats = stats;
        this.grade = grade;
    }

    // Getters and setters
    public CompositeStats getStats() {
        return stats;
    }

    public void setStats(CompositeStats stats) {
        this.stats = stats;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }
}

