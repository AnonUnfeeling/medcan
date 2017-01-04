package ua.softgroup.medreview.web.controller.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import ua.softgroup.medreview.persistent.entity.Note;
import ua.softgroup.medreview.persistent.entity.SubSubject;
import ua.softgroup.medreview.persistent.entity.Subject;
import ua.softgroup.medreview.persistent.repository.NoteRepository;
import ua.softgroup.medreview.persistent.repository.RecordRepository;
import ua.softgroup.medreview.service.SubSubjectService;
import ua.softgroup.medreview.service.SubjectService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Oleksandr Tyshkovets <sg.olexander@gmail.com>
 */
@RestController
public class NoteRestController {

    private final NoteRepository noteRepository;

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private SubSubjectService subSubjectService;

    @Autowired
    public NoteRestController(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @GetMapping("/records/notes")
    public ResponseEntity<Collection<Note>> getNotes(@RequestParam String title) {
        return new ResponseEntity<>(noteRepository.findByRecordTitle(title), HttpStatus.OK);
    }

    @GetMapping(value = "/records/note")
    public ModelAndView getNoteView(@RequestParam String title, @RequestParam String type) {
        return new ModelAndView("note");
    }

    @PostMapping("/records/note/add")
    public
    @ResponseBody
    ResponseEntity makeNewNote(@RequestParam String titleRecord,
                               @RequestParam String description,
                               @RequestParam String conclusion,
                               @RequestParam String keywords,
                               @RequestParam String subject,
                               @RequestParam String subSubject,
                               @RequestParam String country,
                               @RequestParam String language) {
        try {
            Note note = new Note();
            note.setDescription(description);
            note.setConclusion(conclusion);
            note.setKeywords(keywords);
            note.setSubject(subject);
            note.setSubSubject(subSubject);
            note.setCountry(country);
            note.setLanguage(language);
            note.setStatus("In review");
            note.setRecord(recordRepository.findByTitle(titleRecord));
            noteRepository.save(note);
            return ResponseEntity.ok(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(Keys.EMPTY.toString(), HttpStatus.OK);
        }
    }

    @PostMapping("/records/note/edit")
    public
    @ResponseBody
    ResponseEntity editNote(@RequestParam String id,
                            @RequestParam String titleRecord,
                            @RequestParam String description,
                            @RequestParam String conclusion,
                            @RequestParam String keywords,
                            @RequestParam String subject,
                            @RequestParam String subSubject,
                            @RequestParam String country,
                            @RequestParam String language,
                            @RequestParam String status) {
        try {
            Note note = noteRepository.findOne(Long.parseLong(id));
            note.setDescription(description);
            note.setConclusion(conclusion);
            note.setKeywords(keywords);
            note.setSubject(subject);
            note.setSubSubject(subSubject);
            note.setCountry(country);
            note.setLanguage(language);
            note.setStatus(status);
            note.setRecord(recordRepository.findByTitle(titleRecord));
            noteRepository.save(note);
            return ResponseEntity.ok(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(Keys.EMPTY.toString(), HttpStatus.OK);
        }
    }

    @PostMapping("/records/note/remove")
    public
    @ResponseBody
    ResponseEntity removeNote(@RequestParam String id) {
        try {
            noteRepository.delete(Long.parseLong(id));
            return ResponseEntity.ok(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(Keys.FAIL.toString(), HttpStatus.OK);
        }
    }

    @PostMapping("/records/note/get")
    public
    @ResponseBody
    Note getNote(@RequestParam String id) {
        return noteRepository.findOne(Long.parseLong(id));
    }

    private boolean isDeleted(long id, Note note) {
        return noteRepository.findByRecordId(id).remove(note);
    }

    @PostMapping("/records/{id}/chengeStatus")
    public
    @ResponseBody
    ResponseEntity chengeStatus(@PathVariable Long id, @RequestParam Note note, @RequestParam String status) {
        if (status != null && note != null) {
            noteRepository.findByRecordId(id).stream().filter(n -> note.getDescription().equals(n.getDescription())).findFirst().get().setStatus(status);
            return ResponseEntity.ok(HttpStatus.OK);
        } else {
            return new ResponseEntity(Keys.FAIL.toString(), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "getStatus")
    public
    @ResponseBody
    List<String> getAllRoles() {
        List<String> status = new ArrayList<>();
        status.add("In review");
        status.add("Approved");
        status.add("Disapproved");
        status.add("Removed");
        return status;
    }

    @GetMapping("/getSubject")
    public
    @ResponseBody
    List<Subject> getSubject() {
        return subjectService.getAll();
    }

    @GetMapping("/getSubSubject")
    public
    @ResponseBody
    List<SubSubject> getSubSubject() {
        return subSubjectService.getAll();
    }
}
