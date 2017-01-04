package ua.softgroup.medreview.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ua.softgroup.medreview.persistent.entity.Role;
import ua.softgroup.medreview.persistent.entity.User;
import ua.softgroup.medreview.service.AuthenticationService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sergiy Perevyazko <sg.sergiyp@gmail.com>
 */
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public User getPrincipal() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Override
    public String determineHomeUrlByRole() {
        List<String> roles = new ArrayList<>();
        SecurityContextHolder.getContext().getAuthentication().getAuthorities().forEach(authority -> roles.add(authority.getAuthority()));
        return determineUrlByRoles(roles);
    }

    private String determineUrlByRoles(List<String> roles) {
        logger.debug("determineUrlByRoles: " + roles);
        if (roles.contains(Role.ADMIN.name())) {
            return "/adminLogin";
        }
        if (roles.contains(Role.COMPANY.name())) {
            return "/companyLogin";
        }
        if (roles.contains(Role.USER.name())) {
            return "/userLogin";
        }
        return "/error";
    }
}
