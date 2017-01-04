package ua.softgroup.medreview.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.softgroup.medreview.persistent.entity.Company;
import ua.softgroup.medreview.persistent.entity.User;
import ua.softgroup.medreview.persistent.repository.CompanyRepository;
import ua.softgroup.medreview.persistent.repository.UserRepository;
import ua.softgroup.medreview.service.UserService;
import ua.softgroup.medreview.web.dto.UserDto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Sergiy Perevyazko <sg.sergiyp@gmail.com>
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CompanyRepository companyRepository;

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
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

    @Override
    public Page<UserDto> getUserDtos(Pageable pageable) {
        return null;
    }

    @Override
    public List<UserDto> getAllUserDtos() {
        return null;
    }

    private UserDto convertToDto(User user) {
        return new UserDto(user.getLogin(), user.getRoles().get(0).getRole(), user.getCompany().getName());
    }

    private User convertToEntity(UserDto userDto) {
        User user = userRepository.findByLogin(userDto.getLogin());
        if (user == null) user = new User();
        user.setLogin(userDto.getLogin());
//        user.setRoles(new ArrayList<>(Arrays.asList(new )));
        return user;
    }
}
