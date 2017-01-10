package ua.softgroup.medreview.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.softgroup.medreview.persistent.entity.Company;
import ua.softgroup.medreview.persistent.entity.Role;
import ua.softgroup.medreview.persistent.entity.User;
import ua.softgroup.medreview.persistent.entity.UserRole;
import ua.softgroup.medreview.persistent.repository.CompanyRepository;
import ua.softgroup.medreview.persistent.repository.UserRepository;
import ua.softgroup.medreview.service.UserService;
import ua.softgroup.medreview.web.dto.UserDto;
import ua.softgroup.medreview.web.form.UserForm;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static ua.softgroup.medreview.persistent.entity.Role.*;

/**
 * @author Sergiy Perevyazko <sg.sergiyp@gmail.com>
 */
@Service
public class UserServiceImpl implements UserService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, CompanyRepository companyRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void createUser(UserForm userForm) {
        logger.debug("create User from UserForm");
        userRepository.save(convertUserFormToEntity(userForm));
    }

    @Override
    public void updateUser(UserDto userDto) {
        logger.debug("create User from UserDto");
        userRepository.save(convertDtoToEntity(userDto));
    }

    @Override
    public User findUserByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.delete(id);
    }

    @Override
    public void deleteUserByLogin(String name) {
        userRepository.deleteByLogin(name);
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Page<User> getUsersByCompany(Company company, Pageable pageable) {
        return userRepository.findByCompany(company, pageable);
    }

    @Override
    public List<User> findAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    private User convertDtoToEntity(UserDto userDto) {
        logger.debug("convertDtoToEntity: {}", userDto);
        User user = userRepository.findByLogin(userDto.getPreLogin());
        user.setLogin(userDto.getLogin());
        if (userDto.getPassword() != null && !userDto.getPassword().trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }
        setUserRoles(user, userDto.getRole());
        user.setCompany(companyRepository.findByName(userDto.getCompany()));
        return user;
    }

    private User convertUserFormToEntity(UserForm userForm) {
        logger.debug("convertUserFormToEntity: {}", userForm);
        User user = new User();
        user.setLogin(userForm.getLogin());
        String pass = userForm.getPassword();
        if (pass != null && !pass.trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(pass));
        }
        user.setCompany(companyRepository.findByName(userForm.getCompany()));
        user.setLanguage(userForm.getLanguage());
        setUserRoles(user, Role.valueOf(userForm.getRole()));
        return user;
    }

    private void setUserRoles(User user, Role role) {
        if (ADMIN.equals(role)) {
            user.setRoles(Arrays.asList(new UserRole(ADMIN), new UserRole(COMPANY)));
        } else if (COMPANY.equals(role)) {
            user.setRoles(Collections.singletonList(new UserRole(COMPANY)));
        } else user.setRoles(Collections.singletonList(new UserRole(USER)));
    }
}
