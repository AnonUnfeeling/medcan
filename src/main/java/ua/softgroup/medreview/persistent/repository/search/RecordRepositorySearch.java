package ua.softgroup.medreview.persistent.repository.search;

import ua.softgroup.medreview.persistent.entity.Record;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Oleksandr Tyshkovets <sg.olexander@gmail.com>
 */
public interface RecordRepositorySearch {

    /**
     * Searches records by title's keywords
     *
     * @param keywords the keywords to search, separate by blanks
     * @param from optional date range (start)
     * @param to optional date range (end)
     * @return a list of matching records
     */
    List<Record> searchByTitle(String keywords, LocalDate from, LocalDate to);

}
