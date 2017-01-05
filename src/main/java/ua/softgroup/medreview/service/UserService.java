package ua.softgroup.medreview.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.softgroup.medreview.persistent.entity.Company;
import ua.softgroup.medreview.persistent.entity.User;
import ua.softgroup.medreview.web.dto.UserDto;
import ua.softgroup.medreview.web.form.UserForm;

import java.util.List;

/**
 * @author Sergiy Perevyazko <sg.sergiyp@gmail.com>
 */
public interface UserService {

    void saveUser(User user);

    void createUser(UserForm userForm);

    void updateUser(UserDto userDto);

    User findUserByLogin(String login);

    void deleteUserById(Long id);

    void deleteUserByLogin(String login);

    Page<User> findAll(Pageable pageable);

    Page<User> getUsersByCompany(Company company, Pageable pageable);

    List<User> findAllUsers();

}
