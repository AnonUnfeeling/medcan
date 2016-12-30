package ua.softgroup.medreview;

import org.hibernate.search.jpa.Search;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;

/**
 * @author Oleksandr Tyshkovets <sg.olexander@gmail.com>
 */
@Component
public class BuildSearchIndex implements ApplicationListener<ApplicationReadyEvent> {
    private static final Logger log = LoggerFactory.getLogger(BuildSearchIndex.class);

    private final EntityManager entityManager;

    public BuildSearchIndex(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        try {
            Search.getFullTextEntityManager(entityManager).createIndexer().startAndWait();
        } catch (InterruptedException e) {
            log.error("An error occurred when trying to build the search index: ", e);
        }
    }

}
