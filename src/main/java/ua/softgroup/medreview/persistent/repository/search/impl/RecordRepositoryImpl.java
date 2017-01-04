package ua.softgroup.medreview.persistent.repository.search.impl;

import org.hibernate.search.jpa.Search;
import org.springframework.transaction.annotation.Transactional;
import ua.softgroup.medreview.persistent.entity.Record;
import ua.softgroup.medreview.persistent.repository.search.RecordRepositorySearch;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

/**
 * @author Oleksandr Tyshkovets <sg.olexander@gmail.com>
 */
public class RecordRepositoryImpl extends SearchableRepository<Record> implements RecordRepositorySearch {

    private static final String RECORD_TITLE_FIELD = "title";
    private static final String RECORD_CREATION_DATE_FIELD = "creationDate";
    private static final String RECORD_AUTHOR_USERNAME_FIELD = "author.login";

    private final EntityManager entityManager;

    public RecordRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Record> searchByTitle(final String keywords, final LocalDate from, final LocalDate to) {
        return searchByKeywords(
                Search.getFullTextEntityManager(entityManager),
                from, to, RECORD_CREATION_DATE_FIELD,
                keywords, RECORD_TITLE_FIELD
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<Record> searchByTitleAndAuthor(String username, String text, LocalDate from, LocalDate to) {
        return searchByKeywordsAndAuthorAndDateRange(
                Search.getFullTextEntityManager(entityManager),
                username, RECORD_AUTHOR_USERNAME_FIELD,
                from, to, RECORD_CREATION_DATE_FIELD,
                text, Collections.singletonList(RECORD_TITLE_FIELD)
        );
    }

}
