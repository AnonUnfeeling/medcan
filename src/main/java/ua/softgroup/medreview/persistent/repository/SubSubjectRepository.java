package ua.softgroup.medreview.persistent.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import ua.softgroup.medreview.persistent.entity.SubSubject;

import java.util.List;

/**
 * @author Sergiy Perevyazko <sg.sergiyp@gmail.com>
 */
public interface SubSubjectRepository extends CrudRepository<SubSubject, Long> {

    SubSubject findByName(String name);

    @Transactional
    void deleteByName(String name);
}
