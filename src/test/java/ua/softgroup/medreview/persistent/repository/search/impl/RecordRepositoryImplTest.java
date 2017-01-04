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
import ua.softgroup.medreview.persistent.entity.Role;
import ua.softgroup.medreview.persistent.entity.User;
import ua.softgroup.medreview.persistent.repository.RecordRepository;
import ua.softgroup.medreview.persistent.repository.UserRepository;

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
    @Autowired
    private UserRepository userRepository;

    @Before
    public void setUp() throws Exception {
        User user = new User("user", "1111", Role.USER, null);
        User admin = new User("admin", "1111", Role.ADMIN, null);
        userRepository.save(user);
        userRepository.save(admin);

        createRecord("Search_search gay", LocalDateTime.now().minusDays(50), user);
        createRecord("the Google SEARCH", LocalDateTime.now().minusDays(100), user);
        createRecord("Hibernate search", LocalDateTime.now().minusDays(5), admin);
        createRecord("Lucene Search", LocalDateTime.now(), user);
        createRecord("Medical Cannabis", LocalDateTime.now().minusDays(15), admin);
    }

    @After
    public void tearDown() throws Exception {
        recordRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void searchByTitleAndAuthor() {
        List<Record> userRecords = recordRepository.searchByTitleAndAuthor("user", "search, gays", LocalDate.now().minusDays(60), LocalDate.now());
        assertThat(userRecords).hasSize(2);
        List<Record> adminRecords = recordRepository.searchByTitleAndAuthor("admin", "search", null, null);
        assertThat(adminRecords).hasSize(1);
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

    private void createRecord(String title, LocalDateTime createdAt, User author) {
        Record record = new Record(title, "Type", author);
        recordRepository.save(record);
        record.setCreationDate(createdAt);
        recordRepository.save(record);
    }

}