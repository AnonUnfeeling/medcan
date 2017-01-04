package ua.softgroup.medreview.service;

import ua.softgroup.medreview.persistent.entity.User;

import java.util.List;

/**
 * @author Sergiy Perevyazko <sg.sergiyp@gmail.com>
 */
public interface AuthenticationService {

    User getPrincipal();

    List<String> getUserRoles();

    String determineHomeUrlByRole();
}
