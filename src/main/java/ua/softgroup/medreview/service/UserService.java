package ua.softgroup.medreview.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.softgroup.medreview.persistent.entity.User;

import java.util.List;

/**
 * @author Sergiy Perevyazko <sg.sergiyp@gmail.com>
 */
public interface UserService {

    void saveUser(User user);

    User findUserByLogin(String login);

    void deleteUserById(Long id);

    void deleteUserByLogin(String login);

    Page<User> findAll(Pageable pageable);

    List<User> findAllUsers();
}
