package ua.softgroup.medreview.persistent.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
<<<<<<< HEAD
import ua.softgroup.medreview.persistent.entity.Record;
=======
import org.springframework.transaction.annotation.Transactional;
>>>>>>> 8d9cb6a3be36ac7847ee7264a32bde5c8b105197
import ua.softgroup.medreview.persistent.entity.User;

/**
 * @author Oleksandr Tyshkovets <sg.olexander@gmail.com>
 */
public interface UserRepository extends CrudRepository<User, Long> {

    User findByLogin(String login);

<<<<<<< HEAD
    Page<User> findAll(Pageable pageable);
=======
    @Transactional
    void deleteByLogin(String login);
>>>>>>> 8d9cb6a3be36ac7847ee7264a32bde5c8b105197
}
