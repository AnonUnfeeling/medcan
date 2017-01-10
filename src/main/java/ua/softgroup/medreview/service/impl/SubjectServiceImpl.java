package ua.softgroup.medreview.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.softgroup.medreview.persistent.entity.SubSubject;
import ua.softgroup.medreview.persistent.entity.Subject;
import ua.softgroup.medreview.persistent.entity.Treatment;
import ua.softgroup.medreview.persistent.repository.SubSubjectRepository;
import ua.softgroup.medreview.persistent.repository.SubjectRepository;
import ua.softgroup.medreview.persistent.repository.TreatmentRepository;
import ua.softgroup.medreview.service.SubjectService;
import ua.softgroup.medreview.web.dto.SubSubjectDto;
import ua.softgroup.medreview.web.dto.SubjectDto;
import ua.softgroup.medreview.web.dto.TreatmentDto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Sergiy Perevyazko <sg.sergiyp@gmail.com>
 */
@Service
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;
    private final SubSubjectRepository subSubjectRepository;
    private final TreatmentRepository treatmentRepository;

    @Autowired
    public SubjectServiceImpl(SubjectRepository subjectRepository, SubSubjectRepository subSubjectRepository, TreatmentRepository treatmentRepository) {
        this.subjectRepository = subjectRepository;
        this.subSubjectRepository = subSubjectRepository;
        this.treatmentRepository = treatmentRepository;
    }

    @Override
    public List<Subject> getAllSubjects() {
        return (List<Subject>) subjectRepository.findAll();
    }

    @Override
    public List<SubjectDto> getAllSubjectDtos() {
        return ((List<Subject>) subjectRepository.findAll())
                .stream()
                .map(this::convertSubjectToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<SubSubject> getAllSubSubjects() {
        return (List<SubSubject>) subSubjectRepository.findAll();
    }

    @Override
    public List<SubSubjectDto> getAllSubSubjectsDtos() {
        return ((List<SubSubject>) subSubjectRepository.findAll())
                .stream()
                .map(this::convertSubSubjectToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<SubSubjectDto> getSubSubjectDtosBySubjectName(String name) {
        return subjectRepository.findByName(name).getSubSubjects()
                .stream()
                .map(this::convertSubSubjectToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Subject> getSubjectByName(String name) {
        return Optional.ofNullable(subjectRepository.findByName(name));
    }

    @Override
    public Optional<SubSubject> getSubSubjectByName(String name) {
        return Optional.ofNullable(subSubjectRepository.findByName(name));
    }

    @Override
    public void createSubject(Subject subject) {
        subjectRepository.save(subject);
    }

    @Override
    public void createSubSubject(SubSubjectDto subSubjectDto, Subject subject) {
        subSubjectRepository.save(new SubSubject(subSubjectDto.getName(), subject));
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
    public void editSubject(SubjectDto subjectDto, Subject subjectForEdit) {
        subjectForEdit.setName(subjectDto.getName());
        subjectRepository.save(subjectForEdit);
    }

    @Override
    public void editSubSubject(SubSubjectDto subSubjectDto, SubSubject subSubjectForEdit) {
        subSubjectForEdit.setName(subSubjectDto.getName());
        subSubjectRepository.save(subSubjectForEdit);
    }

    @Override
    public Optional<Treatment> getTreatmentByName(String name) {
        return Optional.ofNullable(treatmentRepository.findByName(name));
    }

    @Override
    public List<TreatmentDto> getAllTreatments() {
        return ((List<Treatment>) treatmentRepository.findAll())
                .stream()
                .map(this::convertTreatmentToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TreatmentDto> getTreatmentsBySubSubject(SubSubject subSubject) {
        return subSubject.getTreatments()
                .stream()
                .map(this::convertTreatmentToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void createTreatment(TreatmentDto treatmentDto, SubSubject subSubject) {
        treatmentRepository.save(new Treatment(treatmentDto.getName(), subSubject));
    }

    @Override
    public void deleteTreatment(Treatment treatment) {
        treatmentRepository.delete(treatment);
    }

    private SubjectDto convertSubjectToDto(Subject subject) {
        return new SubjectDto(subject.getName());
    }

    private SubSubjectDto convertSubSubjectToDto(SubSubject subSubject) {
        return new SubSubjectDto(subSubject.getName());
    }

    private TreatmentDto convertTreatmentToDto(Treatment treatment) {
        return new TreatmentDto(treatment.getName());
    }

}
