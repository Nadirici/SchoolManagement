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
    
    public CompositeStats calculateStats(List<Grade> grades) {

        CompositeStats compositeStats = new CompositeStats();

        for (Grade grade: grades) {
            Double score = grade.getScore();
            LeafStats stats = new LeafStats(score, score, score);
            compositeStats.addComponent(stats);
        }

        return compositeStats;
    }

}
