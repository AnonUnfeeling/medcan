package ua.softgroup.medreview.service;

import ua.softgroup.medreview.persistent.entity.User;

/**
 * @author Sergiy Perevyazko <sg.sergiyp@gmail.com>
 */
public interface UserService {

    User getUserByLogin(String login);
}
