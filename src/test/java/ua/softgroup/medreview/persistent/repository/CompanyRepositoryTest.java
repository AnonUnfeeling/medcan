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
public class CompanyRepositoryTest {

    private static final String COMPANY_TABLE = "company";
    private static final String COMPANY_NAME_FIELD = "name-";
    private static final int COMPANY_COUNT = 20;

    private static final Operation DELETE_ALL = sequenceOf(deleteAllFrom(COMPANY_TABLE));

    private static final Operation INSERT_DATA = sequenceOf(
            insertInto(COMPANY_TABLE)
                    .withGeneratedValue("id", sequence().startingAt(1L))
                    .withGeneratedValue("name", stringSequence(COMPANY_NAME_FIELD).startingAt(1L))
                    .columns().repeatingValues().times(COMPANY_COUNT)
                    .build());

    @Autowired
    private DataSource dataSource;
    @Autowired
    private CompanyRepository companyRepository;

    @Before
    public void setUp() throws Exception {
        new DbSetup(new DataSourceDestination(dataSource), sequenceOf(DELETE_ALL, INSERT_DATA)).launch();
    }

    @After
    public void tearDown() throws Exception {
        new DbSetup(new DataSourceDestination(dataSource), DELETE_ALL).launch();
    }


    @Test
    public void findByName() {
        int id = new Random().nextInt(COMPANY_COUNT - 1) + 1;
        assertNotNull(companyRepository.findByName(COMPANY_NAME_FIELD + id));
    }

    @Test
    public void findByName_notFound() {
        assertNull(companyRepository.findByName(COMPANY_NAME_FIELD + COMPANY_COUNT + 42));
    }

    @Test
    public void deleteByName() {
        int id = new Random().nextInt(COMPANY_COUNT - 1) + 1;
        companyRepository.deleteByName(COMPANY_NAME_FIELD + id);
    }
}