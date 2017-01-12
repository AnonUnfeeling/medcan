package ua.softgroup.medreview.web.controller.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import ua.softgroup.medreview.persistent.entity.Note;
import ua.softgroup.medreview.persistent.entity.NoteStatus;
import ua.softgroup.medreview.persistent.repository.NoteRepository;
import ua.softgroup.medreview.persistent.repository.RecordRepository;
import ua.softgroup.medreview.service.AuthenticationService;
import ua.softgroup.medreview.service.SearchService;
import ua.softgroup.medreview.service.SubjectService;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Oleksandr Tyshkovets <sg.olexander@gmail.com>
 * @author jdroidcoder
 */
@RestController
public class NoteRestController {

    private static final String SEARCH_NOTES_VIEW = "searchNotesResult";
    @Autowired
    private AuthenticationService authenticationService;
    private final NoteRepository noteRepository;
    private final RecordRepository recordRepository;
    private final SubjectService subjectService;
    private final SearchService searchService;

    @Autowired
    public NoteRestController(NoteRepository noteRepository, RecordRepository recordRepository,
                              SubjectService subjectService,
                              SearchService searchService) {
        this.noteRepository = noteRepository;
        this.recordRepository = recordRepository;
        this.subjectService = subjectService;
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
                                      @RequestParam String treatment,
                                      @RequestParam String titleForNote) {
        try {
            Note note = new Note();
            note.setDescription(description);
            note.setConclusion(conclusion);
            note.setKeywords(keywords);
            note.setSubject(subject);
            note.setSubSubject(subSubject);
            note.setCountry(recordRepository.findByTitle(titleRecord).getCountry());
            note.setLanguage(authenticationService.getPrincipal().getLanguage());
            note.setTreatment(treatment);
            note.setTitle(titleForNote);
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
//                                   @RequestParam String country,
                                   @RequestParam String treatment,
                                   @RequestParam String titleForNote) {
        try {
            Note note = noteRepository.findOne(Long.parseLong(id));
            note.setDescription(description);
            note.setConclusion(conclusion);
            note.setKeywords(keywords);
            note.setSubject(subject);
            note.setSubSubject(subSubject);
            note.setCountry(recordRepository.findByTitle(titleRecord).getCountry());
            note.setLanguage(authenticationService.getPrincipal().getLanguage());
            note.setTreatment(treatment);
            note.setTitle(titleForNote);
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

    @GetMapping(value = "notes/statuses")
    public ResponseEntity<List<String>> getNoteStatuses() {
        return ResponseEntity.ok(Arrays.stream(NoteStatus.values())
                .map(NoteStatus::getStatus)
                .collect(Collectors.toList()));
    }

    @GetMapping(value = "/notes/search")
    @ResponseBody
    public ResponseEntity<List<Note>> searchNotes(@RequestParam String keyword) {
        return ResponseEntity.ok(searchService.searchByAllFields(keyword, null, null));
    }

    @GetMapping(value = "/notes/results")
    public ModelAndView showSearchResults() {
        return new ModelAndView(SEARCH_NOTES_VIEW);
    }

    @PostMapping(value = "/search/records/{title}/notes/")
    public ResponseEntity<List<Note>> searchNotesInCurrentRecord(@PathVariable String title, @RequestParam String text,
                                                                 @RequestParam String category, @RequestParam String subCategory,
                                                                 @RequestParam String treatment) {

        List<Note> notes = searchService.searchNotesInRecord(title, text, category, subCategory, treatment);
        return ResponseEntity.ok(notes);
    }
}
