package ua.softgroup.medreview.persistent.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import ua.softgroup.medreview.persistent.entity.Company;
import ua.softgroup.medreview.persistent.entity.Record;

/**
 * @author Oleksandr Tyshkovets <sg.olexander@gmail.com>
 */
public interface CompanyRepository extends CrudRepository<Company, Long> {

    Company findByName(String name);

    Page<Record> findAll(Pageable pageable);
}
