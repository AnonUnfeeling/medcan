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
import ua.softgroup.medreview.service.SearchService;
import ua.softgroup.medreview.service.SubSubjectService;
import ua.softgroup.medreview.service.SubjectService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Oleksandr Tyshkovets <sg.olexander@gmail.com>
 * @author jdroidcoder
 */
@RestController
public class NoteRestController {

    private static final String SEARCH_NOTES_VIEW = "searchNotesResult";

    private final NoteRepository noteRepository;
    private final RecordRepository recordRepository;
    private final SubjectService subjectService;
    private final SubSubjectService subSubjectService;
    private final SearchService searchService;

    @Autowired
    public NoteRestController(NoteRepository noteRepository, RecordRepository recordRepository,
                              SubjectService subjectService, SubSubjectService subSubjectService,
                              SearchService searchService) {
        this.noteRepository = noteRepository;
        this.recordRepository = recordRepository;
        this.subjectService = subjectService;
        this.subSubjectService = subSubjectService;
        this.searchService = searchService;
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
    @ResponseBody
    public ResponseEntity makeNewNote(@RequestParam String titleRecord,
                               @RequestParam String description,
                               @RequestParam String conclusion,
                               @RequestParam String keywords,
                               @RequestParam String subject,
                               @RequestParam String subSubject,
                               @RequestParam String country,
                               @RequestParam String language,
                               @RequestParam String treatment) {
        try {
            Note note = new Note();
            note.setDescription(description);
            note.setConclusion(conclusion);
            note.setKeywords(keywords);
            note.setSubject(subject);
            note.setSubSubject(subSubject);
            note.setCountry(country);
            note.setLanguage(language);
            note.setTreatment(treatment);
            note.setStatus("In review");
            note.setRecord(recordRepository.findByTitle(titleRecord));
            noteRepository.save(note);
            return ResponseEntity.ok(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(Keys.EMPTY.toString(), HttpStatus.OK);
        }
    }

    @PostMapping("/records/note/edit")
    @ResponseBody
    public ResponseEntity editNote(@RequestParam String id,
                            @RequestParam String titleRecord,
                            @RequestParam String description,
                            @RequestParam String conclusion,
                            @RequestParam String keywords,
                            @RequestParam String subject,
                            @RequestParam String subSubject,
                            @RequestParam String country,
                            @RequestParam String language,
                            @RequestParam String status,
                            @RequestParam String treatment) {
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
            note.setTreatment(treatment);
            note.setRecord(recordRepository.findByTitle(titleRecord));
            noteRepository.save(note);
            return ResponseEntity.ok(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(Keys.EMPTY.toString(), HttpStatus.OK);
        }
    }

    @PostMapping("/records/note/remove")
    @ResponseBody
    public ResponseEntity removeNote(@RequestParam String id) {
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

    @PostMapping("/records/{id}/changeStatus")
    @ResponseBody
    public ResponseEntity changeStatus(@PathVariable Long id, @RequestParam Note note, @RequestParam String status) {
        if (status != null && note != null) {
            noteRepository.findByRecordId(id).stream().filter(n -> note.getDescription().equals(n.getDescription())).findFirst().get().setStatus(status);
            return ResponseEntity.ok(HttpStatus.OK);
        } else {
            return new ResponseEntity(Keys.FAIL.toString(), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "getStatus")
    @ResponseBody
    public List<String> getAllRoles() {
        List<String> status = new ArrayList<>();
        status.add("In review");
        status.add("Approved");
        status.add("Disapproved");
        status.add("Removed");
        return status;
    }

    @GetMapping("/getSubject")
    @ResponseBody
    public List<Subject> getSubject() {
        return subjectService.getAll();
    }

    @GetMapping("/getSubSubject")
    @ResponseBody
    public List<SubSubject> getSubSubject() {
        return subSubjectService.getAll();
    }

    @GetMapping("/getTreatment")
    @ResponseBody
    public List<String> getTreatment() {
        List<String> treatment = new ArrayList<>();
        treatment.add("Treatment1");
        treatment.add("Treatment2");
        treatment.add("Treatment3");
        return treatment;
    }

    @GetMapping(value = "/notes/search")
    public ResponseEntity<List<Note>> searchNotes(@RequestParam String text) {
        return ResponseEntity.ok(searchService.searchByAllFields(text, null, null));
    }

    @GetMapping(value = "/notes/results")
    public String showSearchResults() {
        return SEARCH_NOTES_VIEW;
    }
}
