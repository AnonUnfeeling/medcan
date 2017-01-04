package ua.softgroup.medreview.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.softgroup.medreview.persistent.entity.Record;
import ua.softgroup.medreview.persistent.entity.Role;
import ua.softgroup.medreview.persistent.repository.RecordRepository;
import ua.softgroup.medreview.persistent.repository.NoteRepository;
import ua.softgroup.medreview.service.AuthenticationService;
import ua.softgroup.medreview.service.SearchService;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Sergiy Perevyazko <sg.sergiyp@gmail.com>
 */
@Service
public class SearchServiceImpl implements SearchService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final NoteRepository noteRepository;
    private final RecordRepository recordRepository;
    private final AuthenticationService authenticationService;

    @Autowired
    public SearchServiceImpl(NoteRepository noteRepository, RecordRepository recordRepository, AuthenticationService authenticationService) {
        this.noteRepository = noteRepository;
        this.recordRepository = recordRepository;
        this.authenticationService = authenticationService;
    }

    @Override
    public List<Record> searchByTitle(String text, LocalDate from, LocalDate to) {
        logger.debug("searchByTitle. text:  {}, from: {}, to: {}", text, from, to);
        List<String> userRoles = authenticationService.getUserRoles();
        if (userRoles.contains(Role.ADMIN.name())) {
            return recordRepository.searchByTitle(text, from, to);
        } else if (userRoles.contains(Role.COMPANY.name())) {
            //TODO: must be implemented
            return null;
        } else if (userRoles.contains(Role.USER.name())) {
            return recordRepository.searchByTitleAndAuthor(authenticationService.getPrincipal().getUsername(), text, from, to);
        }
        return null;
    }
}
