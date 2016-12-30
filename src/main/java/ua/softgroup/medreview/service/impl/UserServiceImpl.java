package ua.softgroup.medreview.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.softgroup.medreview.persistent.entity.User;
import ua.softgroup.medreview.persistent.repository.UserRepository;
import ua.softgroup.medreview.service.UserService;

/**
 * @author Sergiy Perevyazko <sg.sergiyp@gmail.com>
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User getUserByLogin(String login) {
        return userRepository.findByLogin(login);
    }
}
