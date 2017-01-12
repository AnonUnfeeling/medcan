package ua.softgroup.medreview.web.controller.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ua.softgroup.medreview.persistent.entity.SubSubject;
import ua.softgroup.medreview.persistent.entity.Subject;
import ua.softgroup.medreview.persistent.entity.Treatment;
import ua.softgroup.medreview.service.SubjectService;
import ua.softgroup.medreview.web.dto.SubSubjectDto;
import ua.softgroup.medreview.web.dto.SubjectDto;
import ua.softgroup.medreview.web.dto.TreatmentDto;
import ua.softgroup.medreview.web.exception.SubjectNotFoundException;
import ua.softgroup.medreview.web.exception.TreatmentNotFoundException;

import javax.validation.Valid;
import java.util.Arrays;
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
    private static final String TREATMENT_WAS_CREATED = "Treatment was created successfully.";
    private static final String TREATMENT_WAS_DELETED = "Treatment was deleted.";

    @GetMapping(value = "/view")
    public ModelAndView getView() {
        return new ModelAndView("category");
    }

    @GetMapping(value = "/view/treatment")
    public ModelAndView getviewTreatment() {
        return new ModelAndView("treatment");
    }

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

    @PostMapping(value = "/editCategory")
    public ResponseEntity editSubject(@Valid SubjectDto subjectDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getFieldErrors().get(0).getDefaultMessage());
        }
        final Subject subjectForEdit = subjectService.getSubjectByName(subjectDto.getOldName())
                                                     .orElseThrow(() -> new SubjectNotFoundException(subjectDto.getOldName()));
        subjectService.editSubject(subjectDto, subjectForEdit);
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
        final Subject subject = subjectService.getSubjectByName(subSubjectDto.getSubject())
                                              .orElseThrow(() -> new SubjectNotFoundException(subSubjectDto.getSubject()));
        subjectService.createSubSubject(subSubjectDto, subject);
        return ResponseEntity.ok(SUB_SUBJECT_WAS_CREATED);
    }

    @PostMapping(value = "/sub/editSubCategory")
    public ResponseEntity editSubSubject(@Valid SubSubjectDto subSubjectDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getFieldErrors().get(0).getDefaultMessage());
        }
        System.out.println(subSubjectDto);
        final SubSubject subSubject = subjectService.getSubSubjectByName(subSubjectDto.getOldName())
                                                    .orElseThrow(() -> new SubjectNotFoundException(subSubjectDto.getSubject()));
        subjectService.editSubSubject(subSubjectDto, subSubject);
        return ResponseEntity.ok(SUB_SUBJECT_WAS_CHANGED);
    }

    @DeleteMapping(value = "/sub/{name}")
    public ResponseEntity deleteSubSubjectByName(@PathVariable String name) {
        subjectService.deleteSubSubjectByName(name);
        return ResponseEntity.ok(SUB_SUBJECT_WAS_DELETED);
    }

    //// TODO: 12.01.2017
    @GetMapping(value = "/treatments/get")
    public ResponseEntity<List<TreatmentDto>> getTreatmentsBySubsubject() {
//        final SubSubject subSubject = subjectService.getSubSubjectByName(subSubjectName)
//                                                    .orElseThrow(() -> new SubjectNotFoundException(subSubjectName));
        return ResponseEntity.ok(subjectService.getAllTreatments());
    }

    //// TODO: 12.01.2017
    @PostMapping(value = "/treatments")
    public ResponseEntity createTreatment(@Valid TreatmentDto treatmentDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getFieldErrors().get(0).getDefaultMessage());
        }
//        final SubSubject subSubject = subjectService.getSubSubjectByName(treatmentDto.getSubSubject())
//                                                    .orElseThrow(() -> new SubjectNotFoundException(treatmentDto.getSubSubject()));
        subjectService.createTreatment(treatmentDto);
        return ResponseEntity.ok(TREATMENT_WAS_CREATED);
    }

    @PostMapping(value = "/treatments/edit")
    public ResponseEntity editTreatment(@Valid TreatmentDto treatmentDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getFieldErrors().get(0).getDefaultMessage());
        }
        final Treatment treatment = subjectService.getTreatmentByName(treatmentDto.getOldName())
                                                  .orElseThrow(() -> new SubjectNotFoundException(treatmentDto.getOldName()));
        subjectService.editTreatment(treatmentDto, treatment);
        return ResponseEntity.ok(TREATMENT_WAS_CREATED);
    }

    @DeleteMapping(value = "/treatments/{treatmentName}")
    public ResponseEntity deleteTreatment(@PathVariable String treatmentName) {
        final Treatment treatment = subjectService.getTreatmentByName(treatmentName)
                                                  .orElseThrow(() -> new TreatmentNotFoundException(treatmentName));
        subjectService.deleteTreatment(treatment);
        return ResponseEntity.ok(TREATMENT_WAS_DELETED);
    }

}
