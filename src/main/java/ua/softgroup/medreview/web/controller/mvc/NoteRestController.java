package ua.softgroup.medreview.web.controller.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

}
