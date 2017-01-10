package ua.softgroup.medreview.persistent.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import ua.softgroup.medreview.persistent.entity.Treatment;

/**
 * @author Sergiy Perevyazko <sg.sergiyp@gmail.com>
 */
public interface TreatmentRepository extends CrudRepository<Treatment, Long> {

    Treatment findByName(String name);

    @Transactional
    void deleteByName(String name);
}
