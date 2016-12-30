package ua.softgroup.medreview.persistent.repository.search.impl;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.transaction.annotation.Transactional;
import ua.softgroup.medreview.persistent.entity.Record;
import ua.softgroup.medreview.persistent.repository.search.RecordRepositorySearch;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * @author Oleksandr Tyshkovets <sg.olexander@gmail.com>
 */
public class RecordRepositoryImpl implements RecordRepositorySearch {

    private static final String RECORD_TITLE_FIELD = "title";
    private static final String RECORD_CREATION_DATE_FIELD = "creationDate";

    private final EntityManager entityManager;

    public RecordRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Record> searchByTitle(final String keywords, final LocalDate from, final LocalDate to) {
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder()
                .forEntity(Record.class)
                .get();

        BooleanJunction<BooleanJunction> junction = createJunction(keywords, queryBuilder);
        addDateRangeToJunction(from, to, queryBuilder, junction);

        //noinspection unchecked
        return fullTextEntityManager
                .createFullTextQuery(junction.createQuery(), Record.class)
                .getResultList();
    }

    private BooleanJunction<BooleanJunction> createJunction(String keywords, QueryBuilder queryBuilder) {
        BooleanJunction<BooleanJunction> junction = queryBuilder.bool();
        junction.must(queryBuilder
                .keyword()
                .onFields(RECORD_TITLE_FIELD)
                .matching(keywords)
                .createQuery()
        );
        return junction;
    }

    private void addDateRangeToJunction(LocalDate from, LocalDate to, QueryBuilder queryBuilder,
                                        BooleanJunction<BooleanJunction> junction) {
        Optional.ofNullable(from)
                .map(LocalDate::atStartOfDay)
                .map(date -> queryBuilder.range().onField(RECORD_CREATION_DATE_FIELD).above(date).createQuery())
                .ifPresent(junction::must);
        Optional.ofNullable(to)
                .map(date -> date.plusDays(1).atStartOfDay())
                .map(date -> queryBuilder.range().onField(RECORD_CREATION_DATE_FIELD).below(date).excludeLimit().createQuery())
                .ifPresent(junction::must);
    }

}
