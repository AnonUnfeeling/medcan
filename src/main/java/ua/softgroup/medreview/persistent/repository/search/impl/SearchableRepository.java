package ua.softgroup.medreview.persistent.repository.search.impl;

import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;

import java.lang.reflect.ParameterizedType;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * @author Oleksandr Tyshkovets <sg.olexander@gmail.com>
 */
@SuppressWarnings("unchecked")
public abstract class SearchableRepository<T> {

    private final Class<T> clazz;

    protected SearchableRepository() {
        clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    protected List<T> searchByKeywords(FullTextEntityManager fullTextEntityManager,
                                       LocalDate from, LocalDate to, String dateField,
                                       String text, List<String> fields) {

        QueryBuilder queryBuilder = getQueryBuilder(fullTextEntityManager);
        BooleanJunction<BooleanJunction> junction = queryBuilder.bool();
        Optional.ofNullable(from)
                .map(date -> aboveRangeQuery(queryBuilder, date, dateField))
                .ifPresent(junction::must);
        Optional.ofNullable(from)
                .map(date -> aboveBelowQuery(queryBuilder, to, dateField))
                .ifPresent(junction::must);
        junction.must(queryBuilder
                .keyword()
                .onFields(fields.toArray(new String[fields.size()]))
                .matching(text)
                .createQuery());

        return fullTextEntityManager
                .createFullTextQuery(junction.createQuery(), clazz)
                .getResultList();
    }

    protected List<T> searchByKeywordsAndFilter(FullTextEntityManager fullTextEntityManager,
                                                String filterText, String filterField,
                                                LocalDate from, LocalDate to, String dateField,
                                                String text, List<String> fields) {

        QueryBuilder queryBuilder = getQueryBuilder(fullTextEntityManager);
        BooleanJunction<BooleanJunction> junction = queryBuilder.bool();
        junction.must(queryBuilder
                .keyword()
                .onField(filterField)
                .matching(filterText)
                .createQuery());
        Optional.ofNullable(from)
                .map(date -> aboveRangeQuery(queryBuilder, date, dateField))
                .ifPresent(junction::must);
        Optional.ofNullable(from)
                .map(date -> aboveBelowQuery(queryBuilder, to, dateField))
                .ifPresent(junction::must);
        junction.must((text == null || text.trim().isEmpty())
                ? createQueryForSearchingByFieldsWithWildcard(queryBuilder, "*", fields)
                : createQueryForSearchingByFields(queryBuilder, text, fields));

        return fullTextEntityManager
                .createFullTextQuery(junction.createQuery(), clazz)
                .getResultList();
    }

    private Query createQueryForSearchingByFields(QueryBuilder queryBuilder, String text, List<String> fields) {
        return queryBuilder
                .keyword()
                .onFields(fields.toArray(new String[fields.size()]))
                .matching(text)
                .createQuery();
    }

    private Query createQueryForSearchingByFieldsWithWildcard(QueryBuilder queryBuilder, String text, List<String> fields) {
        return queryBuilder
                .keyword()
                .wildcard()
                .onFields(fields.toArray(new String[fields.size()]))
                .matching(text)
                .createQuery();
    }

    private QueryBuilder getQueryBuilder(FullTextEntityManager fullTextEntityManager) {
        return fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder()
                .forEntity(clazz)
                .get();
    }

    private Query aboveRangeQuery(QueryBuilder queryBuilder, LocalDate from, String dateField) {
        return queryBuilder.range().onField(dateField).above(from.atStartOfDay()).createQuery();
    }

    private Query aboveBelowQuery(QueryBuilder queryBuilder, LocalDate to, String dateField) {
        return queryBuilder.range().onField(dateField).below(to.plusDays(1).atStartOfDay()).excludeLimit().createQuery();
    }

}
