package ua.softgroup.medreview.persistent.repository;

import org.springframework.data.repository.CrudRepository;
import ua.softgroup.medreview.persistent.entity.Record;

/**
 * @author Oleksandr Tyshkovets <sg.olexander@gmail.com>
 */
public interface RecordRepository extends CrudRepository<Record, Long> {

    Record findByTitle(String name);

}
