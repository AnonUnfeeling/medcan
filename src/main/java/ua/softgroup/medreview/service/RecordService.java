package ua.softgroup.medreview.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.softgroup.medreview.persistent.entity.Record;
import ua.softgroup.medreview.persistent.entity.User;
import ua.softgroup.medreview.web.form.RecordForm;

/**
 * @author Sergiy Perevyazko <sg.sergiyp@gmail.com>
 */
public interface RecordService {

    Record getByTitle(String title);

    Page<Record> getByAuthor(User author, Pageable pageable);

    Page<Record> getAll(Pageable pageable);

    Page<Record> getRecordsByAuthorities(Pageable pageable);

    void saveRecord(Record recordForm);

    void deleteRecordByName(String name);

    Record getRecordByTitle(String title);
}
