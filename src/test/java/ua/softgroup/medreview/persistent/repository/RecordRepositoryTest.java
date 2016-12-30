package ua.softgroup.medreview.persistent.repository;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.operation.Operation;
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
import ua.softgroup.medreview.persistent.entity.User;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.Random;

import static com.ninja_squad.dbsetup.Operations.deleteAllFrom;
import static com.ninja_squad.dbsetup.Operations.insertInto;
import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static com.ninja_squad.dbsetup.generator.ValueGenerators.dateSequence;
import static com.ninja_squad.dbsetup.generator.ValueGenerators.sequence;
import static com.ninja_squad.dbsetup.generator.ValueGenerators.stringSequence;
import static java.time.temporal.ChronoUnit.DAYS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * @author Oleksandr Tyshkovets <sg.olexander@gmail.com>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { SpringDataConfig.class })
@ActiveProfiles("test")
@SpringBootTest
public class RecordRepositoryTest {

    private static final String USER_ROLE_TABLE = "user_role";
    private static final String USER_TABLE = "user";
    private static final Long USER_ID = 2L;
    private static final int USER_COUNT = 3;

    private static final String RECORD_TABLE = "record";
    private static final String RECORD_TITLE_FIELD = "title-";
    private static final int RECORD_COUNT = 20;

    private static final Operation DELETE_ALL = sequenceOf(
            deleteAllFrom(RECORD_TABLE, USER_ROLE_TABLE, USER_TABLE));

    private static final Operation INSERT_DATA = sequenceOf(
            insertInto(USER_TABLE)
                    .withGeneratedValue("id", sequence().startingAt(1L))
                    .withGeneratedValue("login", stringSequence("login-").startingAt(1L))
                    .columns("password", "company_id").repeatingValues("123456", null)
                    .times(USER_COUNT)
                    .build(),
            insertInto(RECORD_TABLE)
                    .withGeneratedValue("id", sequence().startingAt(1L))
                    .withGeneratedValue("title", stringSequence(RECORD_TITLE_FIELD).startingAt(1L))
                    .withGeneratedValue("creation_date", dateSequence().startingAt(LocalDateTime.now()).incrementingBy(-1, DAYS))
                    .columns("type", "author_id").repeatingValues("some-type", USER_ID)
                    .times(RECORD_COUNT)
                    .build());

    @Autowired
    private DataSource dataSource;
    @Autowired
    private RecordRepository recordRepository;

    @Before
    public void setUp() throws Exception {
        new DbSetup(new DataSourceDestination(dataSource), sequenceOf(DELETE_ALL, INSERT_DATA)).launch();
    }
    @After
    public void tearDown() throws Exception {
        new DbSetup(new DataSourceDestination(dataSource), DELETE_ALL).launch();
    }


    @Test
    public void findByTitle() {
        int id = new Random().nextInt(RECORD_COUNT - 1) + 1;
        assertNotNull(recordRepository.findByTitle(RECORD_TITLE_FIELD + id));
    }

    @Test
    public void findByTitle_notFound() {
        assertNull(recordRepository.findByTitle(RECORD_TITLE_FIELD + RECORD_COUNT + 42));
    }

    @Test
    public void findByAuthor() {
//        assertThat(recordRepository.findByAuthor(new User(USER_ID))).hasSize(RECORD_COUNT);
    }

    @Test
    public void findByAuthor_notFound() {
//        assertThat(recordRepository.findByAuthor(new User(42L))).hasSize(0);
    }

}