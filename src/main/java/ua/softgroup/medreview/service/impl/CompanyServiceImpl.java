package ua.softgroup.medreview.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.softgroup.medreview.persistent.entity.Company;
import ua.softgroup.medreview.persistent.entity.Record;
import ua.softgroup.medreview.persistent.repository.CompanyRepository;
import ua.softgroup.medreview.service.CompanyService;

/**
 * @author Sergiy Perevyazko <sg.sergiyp@gmail.com>
 */
@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Override
    public void createCompany(String name) {
        companyRepository.save(new Company(name));
    }

    @Override
    public void deleteCompanyByName(String name) {
        companyRepository.deleteByName(name);
    }

    @Override
    public Company findByName(String name) {
        return companyRepository.findByName(name);
    }

    @Override
    public Page<Record> findAll(Pageable pageable) {
        return companyRepository.findAll(pageable);
    }
}
