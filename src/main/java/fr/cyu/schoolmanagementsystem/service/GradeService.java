package fr.cyu.schoolmanagementsystem.service;

import fr.cyu.schoolmanagementsystem.dao.GenericDAO;
import fr.cyu.schoolmanagementsystem.dao.GradeDAO;
import fr.cyu.schoolmanagementsystem.entity.Grade;
import fr.cyu.schoolmanagementsystem.util.CompositeStats;
import fr.cyu.schoolmanagementsystem.util.LeafStats;

import java.util.List;
import java.util.UUID;

public class GradeService extends GenericServiceImpl<Grade> {

    public GradeService(GenericDAO<Grade> dao) {
        super(dao);
    }

    @Override
    protected UUID getEntityId(Grade grade) {
        return grade.getId();
    }

    public List<Grade> getAllForEnrollment(UUID enrollmentId) {
        return ((GradeDAO) dao).findAllByEnrollmentId(enrollmentId);
    }

    public List<Grade> getAllForAssignment(UUID assignmentId) {
        return ((GradeDAO) dao).findAllByAssignmentId(assignmentId);
    }

    public double getAverage(List<Grade> grades) {
        if (grades.isEmpty()) {
            return 0;
        }

        double totalWeightedScore = 0;
        double totalCoefficients = 0;

        for (Grade grade : grades) {
            double coefficient = grade.getAssignment().getCoefficient();
            totalWeightedScore += coefficient * grade.getScore();
            totalCoefficients += coefficient;
        }

        return (totalCoefficients > 0) ? totalWeightedScore / totalCoefficients : 0.0;
    }

    public double getMin(List<Grade> grades) {
        return grades.stream().mapToDouble(Grade::getScore).min().orElse(0);
    }

    public double getMax(List<Grade> grades) {
        return grades.stream().mapToDouble(Grade::getScore).max().orElse(0);
    }

    public CompositeStats calculateStats(List<Grade> grades) {

        CompositeStats compositeStats = new CompositeStats();

        if (grades.isEmpty()) {
            LeafStats stats = new LeafStats(Double.NaN, Double.NaN, Double.NaN);
            compositeStats = new CompositeStats();
            compositeStats.addComponent(stats);
        } else {
            double average = getAverage(grades);
            double min = getMin(grades);
            double max = getMax(grades);

            LeafStats stats = new LeafStats(average, min, max);

            compositeStats.addComponent(stats);
        }

        return compositeStats;
    }

}
