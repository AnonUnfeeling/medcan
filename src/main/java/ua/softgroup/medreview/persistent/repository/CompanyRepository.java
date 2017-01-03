package ua.softgroup.medreview.persistent.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import ua.softgroup.medreview.persistent.entity.Company;

/**
 * @author Oleksandr Tyshkovets <sg.olexander@gmail.com>
 */
public interface CompanyRepository extends CrudRepository<Company, Long> {

    Company findByName(String name);

<<<<<<< HEAD
    Page<Company> findAll(Pageable pageable);
=======
    Page<Record> findAll(Pageable pageable);

    @Transactional
    void deleteByName(String name);
>>>>>>> 8d9cb6a3be36ac7847ee7264a32bde5c8b105197
}
