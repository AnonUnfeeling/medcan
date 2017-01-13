package ua.softgroup.medreview.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import ua.softgroup.medreview.persistent.entity.Record;
import ua.softgroup.medreview.persistent.entity.Role;
import ua.softgroup.medreview.persistent.entity.User;
import ua.softgroup.medreview.persistent.repository.NoteRepository;
import ua.softgroup.medreview.persistent.repository.RecordRepository;
import ua.softgroup.medreview.service.AuthenticationService;
import ua.softgroup.medreview.service.RecordService;

import java.util.Collection;

/**
 * @author Sergiy Perevyazko <sg.sergiyp@gmail.com>
 */
@Service
public class RecordServiceImpl implements RecordService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private RecordRepository recordRepository;
    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private AuthenticationService authenticationService;
    private static final SimpleGrantedAuthority adminAuthority = new SimpleGrantedAuthority(Role.ADMIN.name());
    private static final SimpleGrantedAuthority companyAuthority = new SimpleGrantedAuthority(Role.COMPANY.name());
    private static final int NUMBER_OF_PAGES = 10;

    @Override
    public Record getByTitle(String title) {
        logger.debug("getByTitle");
        return recordRepository.findByTitle(title);
    }

    @Override
    public Page<Record> getByAuthor(User author, Pageable pageable) {
        logger.debug("getByAuthor");
        return recordRepository.findByAuthor(author, pageable);
    }

    @Override
    public Page<Record> getSortedRecordsByAuthor(User user, int page, String sortDirection, String sortField) {
        logger.debug("getSortedRecordsByAuthor");
        Page<Record> sortedRecords;
        try {
            sortedRecords = getByAuthor(user, new PageRequest(page, NUMBER_OF_PAGES, new Sort(Sort.Direction.valueOf(sortDirection), sortField)));
            logger.debug("successful!");
        } catch (org.springframework.data.mapping.PropertyReferenceException e) {
            logger.debug(":( doesn't work " + e.getMessage());
            sortedRecords = getByAuthor(user, new PageRequest(page, NUMBER_OF_PAGES));
        }
        return sortedRecords;
    }

    @Override
    public Page<Record> getAll(Pageable pageable) {
        logger.debug("getAll");
        return recordRepository.findAll(pageable);
    }

    @Override
    public Page<Record> getAllSortedRecords(int page, String sortDirection, String sortField) {
        Page<Record> sortedRecords;
        try {
            System.out.println(page + " " + sortDirection + " " + sortField);
            sortedRecords = getAll(new PageRequest(page, NUMBER_OF_PAGES, new Sort(Sort.Direction.valueOf(sortDirection), sortField)));
            sortedRecords.forEach(record -> logger.debug(""+record));
        } catch (org.springframework.data.mapping.PropertyReferenceException e) {
            logger.debug("fail");
            sortedRecords = getAll(new PageRequest(page, NUMBER_OF_PAGES));
        }
        return sortedRecords;
    }

    @Override
    public Page<Record> getRecordsByAuthorities(Pageable pageable) {
        User currentUser = authenticationService.getPrincipal();
        Collection<? extends GrantedAuthority> authorities = currentUser.getAuthorities();
        if (authorities.contains(adminAuthority)) {
            return recordRepository.findAll(pageable);
        }
        if (authorities.contains(companyAuthority)) {
            return recordRepository.findByAuthorCompany(currentUser.getCompany(), pageable);
        } else {
            return recordRepository.findByAuthor(currentUser, pageable);
        }
    }

    @Override
    public Page<Record> getSortedRecordsByAuthorities(int page, String sortDirection, String sortField) {
        Page<Record> sortedRecords;
        try {
            sortedRecords = getRecordsByAuthorities(new PageRequest(page, NUMBER_OF_PAGES, new Sort(Sort.Direction.valueOf(sortDirection), sortField)));
        } catch (org.springframework.data.mapping.PropertyReferenceException e) {
            sortedRecords = getRecordsByAuthorities(new PageRequest(page, NUMBER_OF_PAGES));
        }
        return sortedRecords;
    }

    @Override
    public void saveRecord(Record recordForm) {
        recordRepository.save(recordForm);
    }

    @Override
    public void deleteRecordByName(String name) {
        noteRepository.delete(noteRepository.findByRecordTitle(name));
        recordRepository.delete(recordRepository.findByTitle(name));
    }

    @Override
    public Record getRecordByTitle(String title) {
        return recordRepository.findByTitle(title);
    }

    @Override
    public void editRecord(Record record) {
        recordRepository.save(record);
    }
}
