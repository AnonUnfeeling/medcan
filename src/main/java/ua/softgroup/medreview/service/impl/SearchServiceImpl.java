package ua.softgroup.medreview.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.softgroup.medreview.persistent.entity.Note;
import ua.softgroup.medreview.persistent.entity.Record;
import ua.softgroup.medreview.persistent.entity.Role;
import ua.softgroup.medreview.persistent.repository.NoteRepository;
import ua.softgroup.medreview.persistent.repository.RecordRepository;
import ua.softgroup.medreview.service.AuthenticationService;
import ua.softgroup.medreview.service.SearchService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Sergiy Perevyazko <sg.sergiyp@gmail.com>
 * @author Oleksandr Tyshkovets <sg.olexander@gmail.com>
 */
@Service
public class SearchServiceImpl implements SearchService {
    private static final Logger logger = LoggerFactory.getLogger(SearchServiceImpl.class);
    
    private final NoteRepository noteRepository;
    private final RecordRepository recordRepository;
    private final AuthenticationService authenticationService;

    @Autowired
    public SearchServiceImpl(NoteRepository noteRepository,
                             RecordRepository recordRepository,
                             AuthenticationService authenticationService) {
        this.noteRepository = noteRepository;
        this.recordRepository = recordRepository;
        this.authenticationService = authenticationService;
    }

    @Override
    public List<Record> searchRecords(String text, LocalDate from, LocalDate to) {
        logger.debug("Search records by text '{}', from: {}, to: {}", text, from, to);
        List<String> userRoles = authenticationService.getUserRoles();
        if (userRoles.contains(Role.ADMIN.name())) {
            return recordRepository.searchByKeywords(text, from, to);
        }
        return recordRepository.searchByKeywordsAndAuthor(authenticationService.getPrincipal().getUsername(), text, from, to);
    }

    @Override
    public List<Note> searchNotes(String text, LocalDate from, LocalDate to) {
        logger.debug("Search notes by text '{}', from: {}, to: {}", text, from, to);
        List<String> userRoles = authenticationService.getUserRoles();
        if (userRoles.contains(Role.ADMIN.name())) {
            return noteRepository.searchByAllFields(text, from, to);
        }
        return noteRepository.searchByAllFieldsAndAuthor(authenticationService.getPrincipal().getUsername(), text, from, to);
    }

    @Override
    public List<Note> searchNotesInRecord(String recordTitle, String text, String category, String subCategory, String treatment) {
        logger.debug("Search notes in record '{}' by text '{}'", recordTitle, text);
        return noteRepository.searchByAllFieldsInRecord(recordTitle, text).stream()
                .peek(System.out::println)
                .filter(note -> category == null || category.isEmpty() || note.getSubject().equals(category))
                .filter(note -> subCategory == null || subCategory.isEmpty() || note.getSubSubject().equals(subCategory))
                .filter(note -> treatment == null || treatment.isEmpty() || note.getTreatment().equals(treatment))
                .collect(Collectors.toList());
    }
}
