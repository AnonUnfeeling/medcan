package ua.softgroup.medreview.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import ua.softgroup.medreview.persistent.entity.Record;
import ua.softgroup.medreview.persistent.entity.Role;
import ua.softgroup.medreview.persistent.entity.User;
import ua.softgroup.medreview.persistent.repository.RecordRepository;
import ua.softgroup.medreview.service.AuthenticationService;
import ua.softgroup.medreview.service.RecordService;
import ua.softgroup.medreview.web.form.RecordForm;

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
    private AuthenticationService authenticationService;
    private final SimpleGrantedAuthority adminAuthority = new SimpleGrantedAuthority(Role.ADMIN.name());
    private final SimpleGrantedAuthority companyAuthority = new SimpleGrantedAuthority(Role.COMPANY.name());

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
    public Page<Record> getAll(Pageable pageable) {
        logger.debug("getAll");
        return recordRepository.findAll(pageable);
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
    public void saveRecord(Record recordForm) {
        User currentUser = authenticationService.getPrincipal();
        recordRepository.save(new Record(recordForm.getTitle(), recordForm.getType(), currentUser));
    }

    @Override
    public void deleteRecordByName(String name) {
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
