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
import ua.softgroup.medreview.persistent.repository.NoteRepository;
import ua.softgroup.medreview.persistent.repository.RecordRepository;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * @author Oleksandr Tyshkovets <sg.olexander@gmail.com>
 */
@RestController
public class NoteRestController {

    private final NoteRepository noteRepository;

    @Autowired
    private RecordRepository recordRepository;

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
//        if (title == null && note == null) {
//            return new ResponseEntity(Keys.EMPTY.toString(), HttpStatus.OK);
//        } else if (isCreate(id, note)) {

//        } else {
        Note note = new Note();
        note.setDescription(description);
        note.setConclusion(conclusion);
        note.setKeywords(keywords);
        note.setStatus(subject);
        note.setSubSubject(subSubject);
        note.setCountry(country);
        note.setLanguage(language);
        note.setStatus("Test Status");
        note.setCreationDate(LocalDateTime.now());
        note.setRecord(recordRepository.findByTitle(titleRecord));
        noteRepository.save(note);
        return ResponseEntity.ok(HttpStatus.OK);
//        }
    }

    @PostMapping("/records/{id}/remove")
    public
    @ResponseBody
    ResponseEntity removeNote(@PathVariable Long id, @RequestParam Note note) {
        if (id == 0 && note == null) {
            return new ResponseEntity(Keys.EMPTY.toString(), HttpStatus.OK);
        } else if (isDeleted(id, note)) {
            return ResponseEntity.ok(HttpStatus.OK);
        } else {
            return new ResponseEntity(Keys.FAIL.toString(), HttpStatus.OK);
        }
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
}
