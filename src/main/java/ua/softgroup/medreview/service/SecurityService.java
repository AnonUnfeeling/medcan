package ua.softgroup.medreview.service;

public interface SecurityService {

    Boolean hasAdminAccess();

    Boolean hasUserAccess();

    Boolean hasCompanyAccess();
}
