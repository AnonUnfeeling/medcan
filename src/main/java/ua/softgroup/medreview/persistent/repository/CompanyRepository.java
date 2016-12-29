package ua.softgroup.medreview.persistent.repository;

import org.springframework.data.repository.CrudRepository;
import ua.softgroup.medreview.persistent.entity.Company;

/**
 * @author Oleksandr Tyshkovets <sg.olexander@gmail.com>
 */
public interface CompanyRepository extends CrudRepository<Company, Long> {

    Company findByName(String name);

}
