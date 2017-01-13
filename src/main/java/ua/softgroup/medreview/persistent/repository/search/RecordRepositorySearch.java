package ua.softgroup.medreview.persistent.repository.search;

import ua.softgroup.medreview.persistent.entity.Record;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository for searching records. Supports stemming in English.
 *
 * @author Oleksandr Tyshkovets <sg.olexander@gmail.com>
 */
public interface RecordRepositorySearch {

    /**
     * Searches records by record's {@code title}, {@code endConclusion}, {@code endDescription}
     * and {@code creationDate} fields
     *
     * @param keywords the keywords to search, separate by blanks
     * @param from optional date range (start)
     * @param to optional date range (end)
     * @return a list of matching records
     */
    List<Record> searchByKeywords(String keywords, LocalDate from, LocalDate to);

    /**
     * Searches {@code username}'s records by record's {@code title}, {@code endConclusion}, {@code endDescription}
     * and {@code creationDate} fields
     *
     * @param username the author's username of records
     * @param text the text to search
     * @param from optional date range (start)
     * @param to optional date range (end)
     * @return a list of matching records
     */
    List<Record> searchByKeywordsAndAuthor(String username, String text, LocalDate from, LocalDate to);

}
