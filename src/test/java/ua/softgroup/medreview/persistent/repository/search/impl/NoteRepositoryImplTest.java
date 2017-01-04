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
import ua.softgroup.medreview.persistent.entity.Note;
import ua.softgroup.medreview.persistent.repository.NoteRepository;

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
public class NoteRepositoryImplTest {

    @Autowired
    private NoteRepository noteRepository;

    @Before
    public void setUp() throws Exception {
        createNote("Genetic testing looks for specific inherited changes in a person’s chromosomes, genes, or proteins",
                   "Genetic testing of tumor samples can also be performed, but this doc does not cover such testing",
                   "cancer, gene, testing", "What is genetic testing?", "Genetic testing", "England", "english", "approved",
                   LocalDateTime.now().minusDays(10));
        createNote("Increasing weight has been found to be associated with an increase in the risk of thyroid cancer",
                   "It is unclear what the mechanism might be", "cancer, overweight, obesity",
                   "Obesity and Cancer Risk", "How common is overweight or obesity?", "Belgium", "english", "disapproved",
                   LocalDateTime.now().minusDays(100));
        createNote("Difficulties interpreting homosexuality in different cultures",
                   "Contemporary scholars caution against applying modern Western assumptions about sex and gender to other times and places",
                   "homosexuality, gay, LGBT", "Societal attitudes toward homosexuality", "Homosexuality acceptance",
                   "Ukraine", "english", "Removed", LocalDateTime.now().minusDays(50));
        createNote("Many people don't realize this -- but football is kind of a gay sport",
                   "Football is for gays", "gays, football, cancer", "10 Reasons Football is Gay", "Football is Gay",
                   "England", "english", "approved", LocalDateTime.now());
    }

    @After
    public void tearDown() throws Exception {
        noteRepository.deleteAll();
    }

    @Test
    public void searchByAllFields() throws Exception {
        assertThat(noteRepository.searchByAllFields("genetic, test", null, null)).hasSize(1);
        assertThat(noteRepository.searchByAllFields("english", null, null)).hasSize(4);
    }

    @Test
    public void searchByTitle_notFound() {
        assertThat(noteRepository.searchByAllFields("apple", null, null)).hasSize(0);
    }

    @Test
    public void searchByAllFields_withStemming() throws Exception {
        assertThat(noteRepository.searchByAllFields("social, gays", null, null)).hasSize(2);
    }

    @Test
    public void searchByAllFields_withDate() {
        List<Note> noteList = noteRepository.searchByAllFields("social, gays", LocalDate.now().minusDays(49), LocalDate.now());
        assertThat(noteList).hasSize(1);
    }

    private void createNote(String description, String conclusion, String keywords, String subject, String subSubject,
                            String country, String language, String status, LocalDateTime createdAt) {
        Note note = new Note(description, conclusion, keywords, subject, subSubject, country, language, status);
        noteRepository.save(note);
        note.setCreationDate(createdAt);
        noteRepository.save(note);
    }

}