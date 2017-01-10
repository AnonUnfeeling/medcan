package ua.softgroup.medreview.service;

import ua.softgroup.medreview.persistent.entity.SubSubject;
import ua.softgroup.medreview.persistent.entity.Subject;
import ua.softgroup.medreview.web.dto.SubSubjectDto;
import ua.softgroup.medreview.web.dto.SubjectDto;

import java.util.List;

/**
 * @author Sergiy Perevyazko <sg.sergiyp@gmail.com>
 */
public interface SubjectService {

    List<Subject> getAllSubjects();

    List<SubSubject> getAllSubSubjects();

    Subject getSubjectByName(String name);

    void createSubject(Subject subject);

    void createSubSubject(SubSubject subSubject);

    void createSubSubject(SubSubjectDto subSubjectDto);

    void deleteSubjectByName(String name);

    void deleteSubSubjectByName(String name);

    void editSubject(SubjectDto subjectDto);

    void editSubSubject(SubSubjectDto subSubjectDto);

}
