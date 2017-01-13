package ua.softgroup.medreview.service;

import ua.softgroup.medreview.persistent.entity.Note;
import ua.softgroup.medreview.persistent.entity.Record;

import java.time.LocalDate;
import java.util.List;

/**
 * Search service for searching records and notes. Supports stemming in English.
 *
 * @author Oleksandr Tyshkovets <sg.olexander@gmail.com>
 * @author Sergiy Perevyazko <sg.sergiyp@gmail.com>
 */
public interface SearchService {

    /**
     * Searches records
     *
     * @param text a text to search
     * @param from an optional date range (start)
     * @param to an optional date range (end)
     * @return a list of matching records
     */
    List<Record> searchRecords(String text, LocalDate from, LocalDate to);

    /**
     * Searches notes by all fields
     *
     * @param text a text to search
     * @param from an optional date range (start)
     * @param to an optional date range (end)
     * @return a list of matching notes
     */
    List<Note> searchNotes(String text, LocalDate from, LocalDate to);

    /**
     * Searches notes in the specified record with filtering by {@code category}, {@code subCategory} and {@code treatment}
     *
     * @param recordTitle the record's title
     * @param text the text to search
     * @param category the category to filter
     * @param subCategory the category to filter
     * @param treatment the category to filter
     * @return a list of matching notes in the specified record
     */
    List<Note> searchNotesInRecord(String recordTitle, String text, String category, String subCategory, String treatment);

}
