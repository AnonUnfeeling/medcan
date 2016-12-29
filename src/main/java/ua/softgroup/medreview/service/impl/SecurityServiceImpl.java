package ua.softgroup.medreview.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ua.softgroup.medreview.persistent.entity.Role;
import ua.softgroup.medreview.service.SecurityService;

@Service
public class SecurityServiceImpl implements SecurityService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final SimpleGrantedAuthority adminAuthority = new SimpleGrantedAuthority(Role.ADMIN.name());
    private final SimpleGrantedAuthority userAuthority = new SimpleGrantedAuthority(Role.USER.name());
    private final SimpleGrantedAuthority companyAuthority = new SimpleGrantedAuthority(Role.COMPANY.name());

    @Override
    public Boolean hasAdminAccess() {
        logger.debug("hasAdminAccess?");
        return SecurityContextHolder.getContext()
                .getAuthentication().getAuthorities().contains(adminAuthority);
    }

    @Override
    public Boolean hasUserAccess() {
        logger.debug("hasUserAccess?");
        return SecurityContextHolder.getContext()
                .getAuthentication().getAuthorities().contains(userAuthority);
    }

    @Override
    public Boolean hasCompanyAccess() {
        logger.debug("hasCompanyAccess?");
        return SecurityContextHolder.getContext()
                .getAuthentication().getAuthorities().contains(companyAuthority);
    }
}
