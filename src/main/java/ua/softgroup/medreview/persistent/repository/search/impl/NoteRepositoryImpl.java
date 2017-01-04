package ua.softgroup.medreview.persistent.repository.search.impl;

import org.hibernate.search.jpa.Search;
import org.springframework.transaction.annotation.Transactional;
import ua.softgroup.medreview.persistent.entity.Note;
import ua.softgroup.medreview.persistent.repository.search.NoteRepositorySearch;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * @author Oleksandr Tyshkovets <sg.olexander@gmail.com>
 */
public class NoteRepositoryImpl extends SearchableRepository<Note> implements NoteRepositorySearch {

    private static final String NOTE_DESCRIPTION_FIELD = "description";
    private static final String NOTE_CONCLUSION_FIELD = "conclusion";
    private static final String NOTE_KEYWORDS_FIELD = "keywords";
    private static final String NOTE_SUBJECT_FIELD = "subject";
    private static final String NOTE_SUB_SUBJECT_FIELD = "subSubject";
    private static final String NOTE_COUNTRY_FIELD = "country";
    private static final String NOTE_LANGUAGE_FIELD = "language";
    private static final String NOTE_STATUS_FIELD = "status";
    private static final String NOTE_CREATION_DATE_FIELD = "creationDate";
    private static final String NOTE_AUTHOR_USERNAME_FIELD = "record.author.login";

    private final EntityManager entityManager;

    public NoteRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Deprecated
    public List<Note> searchByKeywords(String text, LocalDate from, LocalDate to) {
        return searchByKeywords(
                Search.getFullTextEntityManager(entityManager),
                from, to, NOTE_CREATION_DATE_FIELD,
                text, Arrays.asList(NOTE_DESCRIPTION_FIELD, NOTE_CONCLUSION_FIELD, NOTE_KEYWORDS_FIELD,
                                    NOTE_SUBJECT_FIELD, NOTE_SUB_SUBJECT_FIELD, NOTE_COUNTRY_FIELD,
                                    NOTE_LANGUAGE_FIELD, NOTE_STATUS_FIELD)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<Note> searchByAllFields(String text, LocalDate from, LocalDate to) {
        return searchByKeywords(
                Search.getFullTextEntityManager(entityManager),
                from, to, NOTE_CREATION_DATE_FIELD,
                text, Arrays.asList(NOTE_DESCRIPTION_FIELD, NOTE_CONCLUSION_FIELD, NOTE_KEYWORDS_FIELD,
                        NOTE_SUBJECT_FIELD, NOTE_SUB_SUBJECT_FIELD, NOTE_COUNTRY_FIELD,
                        NOTE_LANGUAGE_FIELD, NOTE_STATUS_FIELD)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<Note> searchByAllFieldsAndAuthor(String username, String text, LocalDate from, LocalDate to) {
        return searchByKeywordsAndAuthorAndDateRange(
                Search.getFullTextEntityManager(entityManager),
                username, NOTE_AUTHOR_USERNAME_FIELD,
                from, to, NOTE_CREATION_DATE_FIELD,
                text, Arrays.asList(NOTE_DESCRIPTION_FIELD, NOTE_CONCLUSION_FIELD, NOTE_KEYWORDS_FIELD,
                                    NOTE_SUBJECT_FIELD, NOTE_SUB_SUBJECT_FIELD, NOTE_COUNTRY_FIELD,
                                    NOTE_LANGUAGE_FIELD, NOTE_STATUS_FIELD)
        );
    }

}
