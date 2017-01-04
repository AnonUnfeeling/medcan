package ua.softgroup.medreview.persistent.repository;

import org.springframework.data.repository.CrudRepository;
import ua.softgroup.medreview.persistent.entity.Subject;

/**
 * @author Sergiy Perevyazko <sg.sergiyp@gmail.com>
 */
public interface SubjectRepository extends CrudRepository<Subject, Long> {
}
