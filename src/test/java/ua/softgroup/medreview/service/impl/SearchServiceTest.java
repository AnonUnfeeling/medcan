package ua.softgroup.medreview.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.context.ActiveProfiles;
import ua.softgroup.medreview.persistent.entity.Note;
import ua.softgroup.medreview.persistent.entity.Record;
import ua.softgroup.medreview.persistent.repository.NoteRepository;
import ua.softgroup.medreview.persistent.repository.RecordRepository;
import ua.softgroup.medreview.service.AuthenticationService;
import ua.softgroup.medreview.service.SearchService;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * @author Oleksandr Tyshkovets <sg.olexander@gmail.com>
 */
@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles("test")
public class SearchServiceTest {

    private static final String TEXT_TO_SEARCH = "description";
    private static final String RECORD_TITLE_1 = "Record 1";

    @Mock
    private NoteRepository noteRepository;
    @Mock
    private RecordRepository recordRepository;
    @Mock
    private AuthenticationService authenticationService;

    private SearchService searchService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        searchService = new SearchServiceImpl(noteRepository, recordRepository, authenticationService);
    }

    @Test
    public void searchNotesInRecord() {
        Note note1 = new Note("Note title 1" , "description", "conclusion", "keyword", "Cancer", "Brain cancer",
                              "Ukraine", "English", "treatment1");
        note1.setRecord(new Record(RECORD_TITLE_1, null, null));
        Note note2 = new Note("Note title 2" , "description", "conclusion", "keyword", "Cancer", "Breast cancer",
                              "Ukraine", "English", "treatment2");
        note2.setRecord(new Record(RECORD_TITLE_1, null, null));
        Note note3 = new Note("Note title 3" , "description", "conclusion", "keyword", "Cancer", "Lung cancer",
                              "Ukraine", "English", "treatment3");
        note3.setRecord(new Record(RECORD_TITLE_1, null, null));
        Note note4 = new Note("Note title 4" , "description", "conclusion", "keyword", "Cancer", "Lung cancer",
                              "Ukraine", "English", "treatment4");
        note4.setRecord(new Record(RECORD_TITLE_1, null, null));
        Note note5 = new Note("Note title 5" , "description", "conclusion", "keyword", "Flu", "Type A",
                              "Ukraine", "English", "treatment5");
        note5.setRecord(new Record(RECORD_TITLE_1, null, null));
        Note note6 = new Note("Note title 6" , "description", "conclusion", "keyword", "Flu", "Type B",
                              "Ukraine", "English", "treatment6");
        note6.setRecord(new Record(RECORD_TITLE_1, null, null));
        when(noteRepository.searchByAllFieldsInRecord(RECORD_TITLE_1, TEXT_TO_SEARCH)).thenReturn(Arrays.asList(note1, note2, note3, note4, note5, note6));

        assertThat(searchService.searchNotesInRecord(RECORD_TITLE_1, TEXT_TO_SEARCH, "Cancer", "Lung cancer", null)).hasSize(2);
        assertThat(searchService.searchNotesInRecord(RECORD_TITLE_1, TEXT_TO_SEARCH, "Cancer", null, null)).hasSize(4);
        assertThat(searchService.searchNotesInRecord(RECORD_TITLE_1, TEXT_TO_SEARCH, null, null, "treatment5")).hasSize(1);
    }

}