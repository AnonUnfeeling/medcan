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

    List<SubjectDto> getAllSubjectDtos();

    List<SubSubject> getAllSubSubjects();

    List<SubSubjectDto> getAllSubSubjectsDtos();

    List<SubSubjectDto> getSubSubjectDtosBySubjectName(String name);

    Subject getSubjectByName(String name);

    SubSubject getSubSubjectByName(String name);

    void createSubject(Subject subject);

    void createSubSubject(SubSubjectDto subSubjectDto, Subject subject);

    void deleteSubjectByName(String name);

    void deleteSubSubjectByName(String name);

    void editSubject(SubjectDto subjectDto, Subject subjectForEdit);

    void editSubSubject(SubSubjectDto subSubjectDto, SubSubject subSubjectForEdit);

}
