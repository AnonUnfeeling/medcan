package ua.softgroup.medreview.service;

import ua.softgroup.medreview.persistent.entity.SubSubject;
import ua.softgroup.medreview.persistent.entity.Subject;
import ua.softgroup.medreview.persistent.entity.Treatment;
import ua.softgroup.medreview.web.dto.SubSubjectDto;
import ua.softgroup.medreview.web.dto.SubjectDto;
import ua.softgroup.medreview.web.dto.TreatmentDto;

import java.util.List;
import java.util.Optional;

/**
 * @author Sergiy Perevyazko <sg.sergiyp@gmail.com>
 */
public interface SubjectService {

    List<Subject> getAllSubjects();

    List<SubjectDto> getAllSubjectDtos();

    List<SubSubject> getAllSubSubjects();

    List<SubSubjectDto> getAllSubSubjectsDtos();

    List<SubSubjectDto> getSubSubjectDtosBySubjectName(String name);

    Optional<Subject> getSubjectByName(String name);

    Optional<SubSubject> getSubSubjectByName(String name);

    void createSubject(Subject subject);

    void createSubSubject(SubSubjectDto subSubjectDto, Subject subject);

    void deleteSubjectByName(String name);

    void deleteSubSubjectByName(String name);

    void editSubject(SubjectDto subjectDto, Subject subjectForEdit);

    void editSubSubject(SubSubjectDto subSubjectDto, SubSubject subSubjectForEdit);

    Optional<Treatment> getTreatmentByName(String name);

    List<TreatmentDto> getAllTreatments();

    List<TreatmentDto> getTreatmentsBySubSubject(SubSubject subSubject);

    void createTreatment(TreatmentDto treatmentDto, SubSubject subSubject);

    void deleteTreatment(Treatment treatment);

}
