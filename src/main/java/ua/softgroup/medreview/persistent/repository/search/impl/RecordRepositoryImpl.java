package ua.softgroup.medreview.persistent.repository.search.impl;

import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.transaction.annotation.Transactional;
import ua.softgroup.medreview.persistent.entity.Record;
import ua.softgroup.medreview.persistent.repository.search.RecordRepositorySearch;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * @author Oleksandr Tyshkovets <sg.olexander@gmail.com>
 */
public class RecordRepositoryImpl implements RecordRepositorySearch {

    private static final String RECORD_TITLE_FIELD = "title";

    private final EntityManager entityManager;

    public RecordRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Record> searchByTitle(final String keywords) {
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder()
                .forEntity(Record.class)
                .get();

        Query query = queryBuilder
                .keyword()
                .onFields(RECORD_TITLE_FIELD)
                .matching(keywords)
                .createQuery();

        //noinspection unchecked
        return fullTextEntityManager.createFullTextQuery(query, Record.class).getResultList();
    }

}
