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
        createNote("Note1", "cancer, gene", LocalDateTime.now().minusDays(100));
        createNote("Note2", "smoking, radiation, viruses", LocalDateTime.now().minusDays(50));
        createNote("Note3", "cancer causing chemicals, obesity", LocalDateTime.now().minusDays(10));
        createNote("Note4", "viruses, infection, bacteria", LocalDateTime.now());
        createNote("Note5", "cancer, disease, pathology, sickness", LocalDateTime.now().minusDays(70));
    }

    @After
    public void tearDown() throws Exception {
        noteRepository.deleteAll();
    }

    @Test
    public void searchByTitle() throws Exception {
        assertThat(noteRepository.searchByKeywords("cancer", null, null)).hasSize(3);
        assertThat(noteRepository.searchByKeywords("cancer, viruses", null, null)).hasSize(5);
    }

    @Test
    public void searchByKeywords_withStemming() throws Exception {
        assertThat(noteRepository.searchByKeywords("sick", null, null)).hasSize(1);
    }

    @Test
    public void searchByTitle_withDate() {
        List<Note> recordList = noteRepository.searchByKeywords("cancer", LocalDate.now().minusDays(80), LocalDate.now());
        assertThat(recordList).hasSize(2);
    }

    @Test
    public void searchByTitle_notFound() {
        assertThat(noteRepository.searchByKeywords("apple", null, null)).hasSize(0);
    }

    private void createNote(String description, String keywords, LocalDateTime createdAt) {
        Note note = new Note();
        note.setDescription(description);
        note.setKeywords(keywords);
        noteRepository.save(note);
//        note.setCreationDate(createdAt);
        noteRepository.save(note);
    }

}