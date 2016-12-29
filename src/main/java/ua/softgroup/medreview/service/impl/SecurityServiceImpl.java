package ua.softgroup.medreview.service.impl;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ua.softgroup.medreview.persistent.entity.Role;
import ua.softgroup.medreview.service.SecurityService;

@Service
public class SecurityServiceImpl implements SecurityService {

    private final SimpleGrantedAuthority adminAuthority = new SimpleGrantedAuthority(Role.ADMIN.toString());

    @Override
    public Boolean hasAdminAccess() {
        return SecurityContextHolder.getContext()
                .getAuthentication().getAuthorities().contains(adminAuthority);
    }
}
