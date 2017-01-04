package ua.softgroup.medreview.persistent.repository.search;

import ua.softgroup.medreview.persistent.entity.Note;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Oleksandr Tyshkovets <sg.olexander@gmail.com>
 */
public interface NoteRepositorySearch {

    @Deprecated
    List<Note> searchByKeywords(String keywords, LocalDate from, LocalDate to);

    /**
     * Searches notes by all fields. Supports stemming in English.
     *
     * @param text the text to search
     * @param from optional date range (start)
     * @param to optional date range (end)
     * @return a list of matching notes
     */
    List<Note> searchByAllFields(String text, LocalDate from, LocalDate to);

    /**
     * Searches {@code username}'s notes by all fields. Supports stemming in English.
     *
     * @param username the author's username of notes
     * @param text the text to search
     * @param from optional date range (start)
     * @param to optional date range (end)
     * @return  a list of matching notes
     */
    List<Note> searchByAllFieldsAndAuthor(String username, String text, LocalDate from, LocalDate to);

}
