package ua.softgroup.medreview.persistent.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import org.springframework.transaction.annotation.Transactional;

import ua.softgroup.medreview.persistent.entity.Company;
import ua.softgroup.medreview.persistent.entity.User;

/**
 * @author Oleksandr Tyshkovets <sg.olexander@gmail.com>
 */
public interface UserRepository extends CrudRepository<User, Long> {

    User findByLogin(String login);

    Page<User> findAll(Pageable pageable);

    Page<User> findByCompany(Company company, Pageable pageable);

    @Transactional
    void deleteByLogin(String login);

}
