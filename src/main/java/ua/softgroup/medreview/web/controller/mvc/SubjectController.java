package ua.softgroup.medreview.web.controller.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.softgroup.medreview.persistent.entity.SubSubject;
import ua.softgroup.medreview.persistent.entity.Subject;
import ua.softgroup.medreview.service.SubjectService;
import ua.softgroup.medreview.web.dto.SubSubjectDto;
import ua.softgroup.medreview.web.dto.SubjectDto;

import javax.validation.Valid;
import java.util.List;

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
    public ResponseEntity<List<Subject>> getAllSubjects() {
        return ResponseEntity.ok(subjectService.getAllSubjects());
    }

    @GetMapping(value = "/{subjectName}")
    public ResponseEntity<List<SubSubject>> getSubSubjectsBySubjectName(@PathVariable String subjectName) {
        return ResponseEntity.ok(subjectService.getSubjectByName(subjectName).getSubSubjects());
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
        subjectService.editSubject(subjectDto);
        return ResponseEntity.ok(SUBJECT_WAS_CHANGED);
    }

    @DeleteMapping(value = "/{subjectName}")
    public ResponseEntity deleteSubjectByName(@PathVariable String subjectName) {
        subjectService.deleteSubjectByName(subjectName);
        return ResponseEntity.ok(SUBJECT_WAS_DELETED);
    }

    @GetMapping(value = "/sub")
    public ResponseEntity<List<SubSubject>> getAllSubSubjects() {
        return ResponseEntity.ok(subjectService.getAllSubSubjects());
    }

    @PostMapping(value = "/sub")
    public ResponseEntity createSubSubject(@Valid SubSubjectDto subSubjectDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getFieldErrors().get(0).getDefaultMessage());
        }
        subjectService.createSubSubject(subSubjectDto);
        return ResponseEntity.ok(SUB_SUBJECT_WAS_CREATED);
    }

    @PutMapping(value = "/sub")
    public ResponseEntity editSubSubject(@Valid SubSubjectDto subSubjectDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getFieldErrors().get(0).getDefaultMessage());
        }
        subjectService.editSubSubject(subSubjectDto);
        return ResponseEntity.ok(SUB_SUBJECT_WAS_CHANGED);
    }

    @DeleteMapping(value = "/sub")
    public ResponseEntity deleteSubSubjectByName(String name) {
        subjectService.deleteSubSubjectByName(name);
        return ResponseEntity.ok(SUB_SUBJECT_WAS_DELETED);
    }

}
