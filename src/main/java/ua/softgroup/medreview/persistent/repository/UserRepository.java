package ua.softgroup.medreview.persistent.repository;

import org.springframework.data.repository.CrudRepository;
import ua.softgroup.medreview.persistent.entity.User;

/**
 * @author Oleksandr Tyshkovets <sg.olexander@gmail.com>
 */
public interface UserRepository extends CrudRepository<User, Long> {

    User findByLogin(String login);

}
