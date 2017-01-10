package ua.softgroup.medreview.web.controller.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.softgroup.medreview.persistent.entity.Subject;
import ua.softgroup.medreview.service.SubjectService;
import ua.softgroup.medreview.web.dto.SubSubjectDto;
import ua.softgroup.medreview.web.dto.SubjectDto;
import ua.softgroup.medreview.web.exception.SubjectNotFoundException;

import javax.validation.Valid;
import java.util.List;

import static java.util.Optional.ofNullable;

/**
 * @author Sergiy Perevyazko <sg.sergiyp@gmail.com>
 */
@RestController
@RequestMapping(value = "/subjects")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;
    private static final String SUBJECT_WAS_CREATED = "Subject was created successfully.";
    private static final String SUBJECT_WAS_CHANGED = "Subject was changed.";
    private static final String SUBJECT_WAS_DELETED = "Subject was deleted successfully.";
    private static final String SUB_SUBJECT_WAS_CREATED = "SubSubject was created successfully.";
    private static final String SUB_SUBJECT_WAS_CHANGED = "SubSubject was changed.";
    private static final String SUB_SUBJECT_WAS_DELETED = "SubSubject was deleted.";

    @GetMapping
    public ResponseEntity<List<SubjectDto>> getAllSubjects() {
        return ResponseEntity.ok(subjectService.getAllSubjectDtos());
    }

    @GetMapping(value = "/{subjectName}")
    public ResponseEntity<List<SubSubjectDto>> getSubSubjectsDtosBySubjectName(@PathVariable String subjectName) {
        return ResponseEntity.ok(subjectService.getSubSubjectDtosBySubjectName(subjectName));
    }

    @PostMapping
    public ResponseEntity createSubject(@Valid SubjectDto subjectDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getFieldErrors().get(0).getDefaultMessage());
        }
        subjectService.createSubject(new Subject(subjectDto.getName()));
        return ResponseEntity.ok(SUBJECT_WAS_CREATED);
    }

    @PutMapping
    public ResponseEntity editSubject(@Valid SubjectDto subjectDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getFieldErrors().get(0).getDefaultMessage());
        }
        subjectService.editSubject(subjectDto, ofNullable(subjectService.getSubjectByName(subjectDto.getOldName()))
                .orElseThrow(() -> new SubjectNotFoundException(subjectDto.getOldName())));
        return ResponseEntity.ok(SUBJECT_WAS_CHANGED);
    }

    @DeleteMapping(value = "/{subjectName}")
    public ResponseEntity deleteSubjectByName(@PathVariable String subjectName) {
        subjectService.deleteSubjectByName(subjectName);
        return ResponseEntity.ok(SUBJECT_WAS_DELETED);
    }

    @GetMapping(value = "/sub")
    public ResponseEntity<List<SubSubjectDto>> getAllSubSubjects() {
        return ResponseEntity.ok(subjectService.getAllSubSubjectsDtos());
    }

    @PostMapping(value = "/sub")
    public ResponseEntity createSubSubject(@Valid SubSubjectDto subSubjectDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getFieldErrors().get(0).getDefaultMessage());
        }
        subjectService.createSubSubject(subSubjectDto, ofNullable(subjectService.getSubjectByName(subSubjectDto.getSubject()))
                .orElseThrow(() -> new SubjectNotFoundException(subSubjectDto.getSubject())));
        return ResponseEntity.ok(SUB_SUBJECT_WAS_CREATED);
    }

    @PutMapping(value = "/sub")
    public ResponseEntity editSubSubject(@Valid SubSubjectDto subSubjectDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getFieldErrors().get(0).getDefaultMessage());
        }
        subjectService.editSubSubject(subSubjectDto, ofNullable(subjectService.getSubSubjectByName(subSubjectDto.getOldName()))
                .orElseThrow(() -> new SubjectNotFoundException(subSubjectDto.getOldName())));
        return ResponseEntity.ok(SUB_SUBJECT_WAS_CHANGED);
    }

    @DeleteMapping(value = "/sub")
    public ResponseEntity deleteSubSubjectByName(String name) {
        subjectService.deleteSubSubjectByName(name);
        return ResponseEntity.ok(SUB_SUBJECT_WAS_DELETED);
    }

}
