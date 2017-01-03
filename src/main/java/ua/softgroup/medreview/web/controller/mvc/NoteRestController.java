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
import ua.softgroup.medreview.persistent.entity.Note;
import ua.softgroup.medreview.persistent.repository.NoteRepository;

import java.util.Collection;

/**
 * @author Oleksandr Tyshkovets <sg.olexander@gmail.com>
 */
@RestController
public class NoteRestController {

    private final NoteRepository noteRepository;

    @Autowired
    public NoteRestController(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @GetMapping("/records/{id}/notes")
    public ResponseEntity<Collection<Note>> getNotes(@PathVariable Long id) {
        return new ResponseEntity<>(noteRepository.findByRecordId(id), HttpStatus.OK);
    }

    @PostMapping("/records/{id}/add")
    public
    @ResponseBody
    ResponseEntity makeNewNote(@PathVariable Long id, @RequestParam Note note) {
        if (id == 0 && note == null) {
            return new ResponseEntity(Keys.EMPTY.toString(), HttpStatus.OK);
        } else if (isCreate(id, note)) {
            return ResponseEntity.ok(HttpStatus.OK);
        } else {
            return new ResponseEntity(Keys.FAIL.toString(), HttpStatus.OK);
        }
    }

    private boolean isCreate(long id, Note note) {
        return noteRepository.findByRecordId(id).add(note);
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
