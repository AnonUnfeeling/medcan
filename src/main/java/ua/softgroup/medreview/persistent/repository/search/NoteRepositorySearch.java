package ua.softgroup.medreview.persistent.repository.search;

import ua.softgroup.medreview.persistent.entity.Note;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository for searching notes. Supports stemming in English.
 *
 * @author Oleksandr Tyshkovets <sg.olexander@gmail.com>
 */
public interface NoteRepositorySearch {

    /**
     * Searches notes by all fields
     *
     * @param text the text to search
     * @param from optional date range (start)
     * @param to optional date range (end)
     * @return a list of matching notes
     */
    List<Note> searchByAllFields(String text, LocalDate from, LocalDate to);

    /**
     * Searches {@code username}'s notes by all fields
     *
     * @param username the author's username of notes
     * @param text the text to search
     * @param from optional date range (start)
     * @param to optional date range (end)
     * @return a list of matching notes
     */
    List<Note> searchByAllFieldsAndAuthor(String username, String text, LocalDate from, LocalDate to);

    /**
     * Searches notes by all fields in the specified record
     *
     * @param recordTitle the record's title
     * @param text the text to search
     * @return a list of matching notes in the specified record
     */
    List<Note> searchByAllFieldsInRecord(String recordTitle, String text);

}
