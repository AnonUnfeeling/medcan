package ua.softgroup.medreview.persistent.repository;

import org.springframework.data.repository.CrudRepository;
import ua.softgroup.medreview.persistent.entity.Record;
import ua.softgroup.medreview.persistent.entity.User;
import ua.softgroup.medreview.persistent.repository.search.RecordRepositorySearch;

import java.util.Set;

/**
 * @author Oleksandr Tyshkovets <sg.olexander@gmail.com>
 */
public interface RecordRepository extends CrudRepository<Record, Long>, RecordRepositorySearch {

    Record findByTitle(String name);

    Set<Record> findByAuthor(User author);

}
