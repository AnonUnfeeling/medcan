package ua.softgroup.medreview.persistent.repository.search;

import ua.softgroup.medreview.persistent.entity.Record;

import java.util.List;

/**
 * @author Oleksandr Tyshkovets <sg.olexander@gmail.com>
 */
public interface RecordRepositorySearch {

    /**
     * Searches records by title's keywords
     *
     * @param keywords the keywords to search, separate by blanks
     * @return a list of matching records
     */
    List<Record> searchByTitle(String keywords);

}
