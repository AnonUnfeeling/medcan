package ua.softgroup.medreview.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.softgroup.medreview.persistent.entity.SubSubject;
import ua.softgroup.medreview.persistent.entity.Subject;
import ua.softgroup.medreview.persistent.repository.SubSubjectRepository;
import ua.softgroup.medreview.persistent.repository.SubjectRepository;
import ua.softgroup.medreview.service.SubjectService;
import ua.softgroup.medreview.web.dto.SubSubjectDto;
import ua.softgroup.medreview.web.dto.SubjectDto;

import java.util.List;

/**
 * @author Sergiy Perevyazko <sg.sergiyp@gmail.com>
 */
@Service
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;
    private final SubSubjectRepository subSubjectRepository;

    @Autowired
    public SubjectServiceImpl(SubjectRepository subjectRepository, SubSubjectRepository subSubjectRepository) {
        this.subjectRepository = subjectRepository;
        this.subSubjectRepository = subSubjectRepository;
    }

    @Override
    public List<Subject> getAllSubjects() {
        return (List<Subject>) subjectRepository.findAll();
    }

    @Override
    public List<SubSubject> getAllSubSubjects() {
        return (List<SubSubject>) subSubjectRepository.findAll();
    }

    @Override
    public Subject getSubjectByName(String name) {
        return subjectRepository.findByName(name);
    }

    @Override
    public void createSubject(Subject subject) {
        subjectRepository.save(subject);
    }

    @Override
    public void createSubSubject(SubSubject subSubject) {
        subSubjectRepository.save(subSubject);
    }

    @Override
    public void createSubSubject(SubSubjectDto subSubjectDto) {
        subSubjectRepository.save(new SubSubject(subSubjectDto.getName(), subjectRepository.findByName(subSubjectDto.getSubject())));
    }

    @Override
    public void deleteSubjectByName(String name) {
        subjectRepository.deleteByName(name);
    }

    @Override
    public void deleteSubSubjectByName(String name) {
        subSubjectRepository.deleteByName(name);
    }

    @Override
    public void editSubject(SubjectDto subjectDto) {
        Subject subject = subjectRepository.findByName(subjectDto.getOldName());
        subject.setName(subjectDto.getName());
        subjectRepository.save(subject);
    }

    @Override
    public void editSubSubject(SubSubjectDto subSubjectDto) {
        SubSubject subSubject = subSubjectRepository.findByName(subSubjectDto.getOldName());
        subSubject.setName(subSubjectDto.getName());
        subSubjectRepository.save(subSubject);
    }

}
