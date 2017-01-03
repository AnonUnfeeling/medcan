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

import javax.sql.DataSource;

import java.util.Random;

import static com.ninja_squad.dbsetup.Operations.deleteAllFrom;
import static com.ninja_squad.dbsetup.Operations.insertInto;
import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static com.ninja_squad.dbsetup.generator.ValueGenerators.sequence;
import static com.ninja_squad.dbsetup.generator.ValueGenerators.stringSequence;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * @author Oleksandr Tyshkovets <sg.olexander@gmail.com>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringDataConfig.class})
@ActiveProfiles("test")
@SpringBootTest
public class UserRepositoryTest {

    private static final String USER_ROLE_TABLE = "user_role";
    private static final String USER_TABLE = "user";
    private static final String USER_LOGIN_FIELD = "login-";
    private static final int USER_COUNT = 20;

    private static final Operation DELETE_ALL = sequenceOf(
            deleteAllFrom(USER_ROLE_TABLE, USER_TABLE));

    private static final Operation INSERT_DATA = sequenceOf(
            insertInto(USER_TABLE)
                    .withGeneratedValue("id", sequence().startingAt(1L))
                    .withGeneratedValue("login", stringSequence(USER_LOGIN_FIELD).startingAt(1L))
                    .columns("password", "company_id").repeatingValues("123456", null)
                    .times(USER_COUNT)
                    .build());

    @Autowired
    private DataSource dataSource;
    @Autowired
    private UserRepository userRepository;

    @Before
    public void setUp() throws Exception {
        new DbSetup(new DataSourceDestination(dataSource), sequenceOf(DELETE_ALL, INSERT_DATA)).launch();
    }

    @After
    public void tearDown() throws Exception {
        new DbSetup(new DataSourceDestination(dataSource), DELETE_ALL).launch();
    }

    @Test
    public void findByLogin() throws Exception {
        int id = new Random().nextInt(USER_COUNT - 1) + 1;
        assertNotNull(userRepository.findByLogin(USER_LOGIN_FIELD + id));
    }

    @Test
    public void findByLogin_notFound() throws Exception {
        assertNull(userRepository.findByLogin(USER_LOGIN_FIELD + USER_COUNT + 42));
    }

    @Test
    public void deleteByLogin() throws Exception {
        int id = new Random().nextInt(USER_COUNT - 1) + 1;
        userRepository.deleteByLogin(USER_LOGIN_FIELD + id);
    }
}