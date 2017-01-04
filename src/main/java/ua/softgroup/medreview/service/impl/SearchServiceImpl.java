package ua.softgroup.medreview.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.softgroup.medreview.persistent.entity.Note;
import ua.softgroup.medreview.persistent.entity.Record;
import ua.softgroup.medreview.persistent.repository.search.NoteRepositorySearch;
import ua.softgroup.medreview.persistent.repository.search.RecordRepositorySearch;
import ua.softgroup.medreview.service.SearchService;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Sergiy Perevyazko <sg.sergiyp@gmail.com>
 */
@Service
public class SearchServiceImpl implements SearchService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final NoteRepositorySearch noteRepositorySearch;
    private final RecordRepositorySearch recordRepositorySearch;

    @Autowired
    public SearchServiceImpl(NoteRepositorySearch noteRepositorySearch, RecordRepositorySearch recordRepositorySearch) {
        this.noteRepositorySearch = noteRepositorySearch;
        this.recordRepositorySearch = recordRepositorySearch;
    }

    @Override
    public List<Note> searchByKeywords(String keywords, LocalDate from, LocalDate to) {
        logger.debug("searchByKeywords. keywords: {}, from: {}, to: {}", keywords, from, to);
        return noteRepositorySearch.searchByKeywords(keywords, from, to);
    }

    @Override
    public List<Record> searchByTitle(String keywords, LocalDate from, LocalDate to) {
        logger.debug("searchByTitle. keywords:  {}, from: {}, to: {}");
        return recordRepositorySearch.searchByTitle(keywords, from, to);
    }
}
