package ua.softgroup.medreview.web.controller.mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import ua.softgroup.medreview.persistent.entity.Company;
import ua.softgroup.medreview.service.CompanyService;

/**
 * Created by jdroidcoder on 28.12.2016.
 */
@RestController
public class CompanyController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CompanyService companyService;

    @PostMapping(value = "makeCompany")
    public ResponseEntity makeCompany(@RequestParam String companyName) {
        logger.debug("makeCompany");
        try {
            companyService.createCompany(companyName);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body(String.format("'%s' is already taken", companyName));
        }
        return ResponseEntity.ok(String.format("Company '%s' has been created successfully.", companyName));
    }

    @GetMapping(value = "company")
    public ModelAndView showAllCompanies() {
        return new ModelAndView("admin");
    }

    @GetMapping(value = "companies")
    public ResponseEntity<Page<Company>> showAllCompanies(int page) {
        logger.debug("show companies from page=" + page);
        return ResponseEntity.ok(companyService.findAll(new PageRequest(page - 1, 10)));
    }

    @PostMapping(value = "removeCompany")
    public ResponseEntity removeCompany(@RequestParam String companyName) {
        logger.debug("removeCompany " + companyName);
        companyService.deleteCompanyByName(companyName);
        return ResponseEntity.ok(String.format("Company '%s' has been deleted.", companyName));
    }

    @GetMapping(value = "getAllCompany")
    public ResponseEntity<Iterable<Company>> getAllCompany() {
        logger.debug("get all company");
        return ResponseEntity.ok(companyService.findAll());
    }
}