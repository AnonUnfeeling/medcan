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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
        createRecord("Search_search gay", LocalDateTime.now().minusDays(50));
        createRecord("the Google SEARCH", LocalDateTime.now().minusDays(100));
        createRecord("Hibernate search", LocalDateTime.now().minusDays(5));
        createRecord("Lucene Search", LocalDateTime.now());
        createRecord("Medical Cannabis", LocalDateTime.now().minusDays(15));
    }

    @After
    public void tearDown() throws Exception {
        recordRepository.deleteAll();
    }

    @Test
    public void searchByTitle() {
        assertThat(recordRepository.searchByTitle("search, cannabis", null, null)).hasSize(4);
    }

    @Test
    public void searchByTitle_withStemming() {
        assertThat(recordRepository.searchByTitle("gays", null, null)).hasSize(1);
    }

    @Test
    public void searchByTitle_withDate() {
        List<Record> recordList = recordRepository.searchByTitle("search, cannabis", LocalDate.now().minusDays(20), LocalDate.now());
        assertThat(recordList).hasSize(3);
    }

    @Test
    public void searchByTitle_notFound() {
        assertThat(recordRepository.searchByTitle("apple", null, null)).hasSize(0);
    }

    private void createRecord(String title, LocalDateTime createdAt) {
        Record record = new Record(title, "Type", null);
        recordRepository.save(record);
        record.setCreationDate(createdAt);
        recordRepository.save(record);
    }

}