package ua.softgroup.medreview.persistent.repository;

import org.springframework.data.repository.CrudRepository;
import ua.softgroup.medreview.persistent.entity.Note;

import java.util.Set;

/**
 * @author Oleksandr Tyshkovets <sg.olexander@gmail.com>
 */
public interface NoteRepository extends CrudRepository<Note, Long> {

    Set<Note> findByRecordId(Long recordId);

}
