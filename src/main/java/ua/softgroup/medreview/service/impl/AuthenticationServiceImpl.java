package ua.softgroup.medreview.service.impl;

import org.springframework.security.core.context.SecurityContextHolder;
import ua.softgroup.medreview.persistent.entity.User;
import ua.softgroup.medreview.service.AuthenticationService;

/**
 * @author Sergiy Perevyazko <sg.sergiyp@gmail.com>
 */
public class AuthenticationServiceImpl implements AuthenticationService {

    @Override
    public User getPrincipal() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
