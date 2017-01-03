package ua.softgroup.medreview.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.softgroup.medreview.persistent.entity.Company;
import ua.softgroup.medreview.persistent.entity.Record;

import java.util.Iterator;

/**
 * @author Sergiy Perevyazko <sg.sergiyp@gmail.com>
 */
public interface CompanyService {

    void createCompany(String name);

    void deleteCompanyByName(String name);

    Iterable<Company> findAll();

    Company findByName(String name);

    Page<Company> findAll(Pageable pageable);
}

