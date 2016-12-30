package ua.softgroup.medreview.persistent.repository.search.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ua.softgroup.medreview.persistent.SpringDataConfig;
import ua.softgroup.medreview.persistent.entity.Record;
import ua.softgroup.medreview.persistent.repository.RecordRepository;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Oleksandr Tyshkovets <sg.olexander@gmail.com>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { SpringDataConfig.class })
@ActiveProfiles("test")
@SpringBootTest
public class RecordRepositoryImplTest {

    @Autowired
    private RecordRepository recordRepository;

    @Before
    public void setUp() throws Exception {
        recordRepository.save(new Record("Search_search", "Type 1", null));
        recordRepository.save(new Record("Google SEARCH", "Type 2", null));
        recordRepository.save(new Record("Hibernate search", "Type 3", null));
        recordRepository.save(new Record("Lucene Search", "Type 4", null));
        recordRepository.save(new Record("Medical Cannabis", "Type 5", null));
    }

    @After
    public void tearDown() throws Exception {
        recordRepository.deleteAll();
    }

    @Test
    public void searchByKeyword() {
        assertThat(recordRepository.searchByTitle("search, cannabis")).hasSize(4);
    }

    @Test
    public void searchByKeyword_notFound() {
        assertThat(recordRepository.searchByTitle("apple")).hasSize(0);
    }

}