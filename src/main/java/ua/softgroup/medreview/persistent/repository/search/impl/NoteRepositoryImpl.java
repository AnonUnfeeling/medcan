package ua.softgroup.medreview.persistent.repository.search.impl;

import org.hibernate.search.jpa.Search;
import org.springframework.transaction.annotation.Transactional;
import ua.softgroup.medreview.persistent.entity.Note;
import ua.softgroup.medreview.persistent.repository.search.NoteRepositorySearch;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

/**
 * @author Oleksandr Tyshkovets <sg.olexander@gmail.com>
 */
public class NoteRepositoryImpl extends SearchableRepository<Note> implements NoteRepositorySearch {

    private static final String NOTE_KEYWORDS_FIELD = "keywords";
    private static final String NOTE_CREATION_DATE_FIELD = "creationDate";

    private final EntityManager entityManager;

    public NoteRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Note> searchByKeywords(String keywords, LocalDate from, LocalDate to) {
        return searchByKeywords(
                Search.getFullTextEntityManager(entityManager),
                from, to, NOTE_CREATION_DATE_FIELD,
                keywords, NOTE_KEYWORDS_FIELD
        );
    }

}
