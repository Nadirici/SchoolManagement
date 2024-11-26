package fr.cyu.schoolmanagementsystem.model.dto;

import fr.cyu.schoolmanagementsystem.model.entity.base.BasePersonEntity;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TeacherDTO extends BasePersonEntity {
    private String department;
}
