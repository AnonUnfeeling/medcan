package ua.softgroup.medreview.web.controller.mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import ua.softgroup.medreview.persistent.entity.Company;
import ua.softgroup.medreview.service.CompanyService;
import ua.softgroup.medreview.web.dto.CompanyDto;

import javax.validation.Valid;

/**
 * Created by jdroidcoder on 28.12.2016.
 */
@RestController
public class CompanyController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final CompanyService companyService;
    private static final int NUMBER_OF_PAGES = 10;
    private static final String COMPANY_WAS_CREATED = "Company \"%s\" was created.";
    private static final String COMPANY_WAS_DELETED = "Company \"%s\" was deleted.";
    private static final String COMPANY_IS_ALREADY_EXIST = "Company \"%s\" is already exist";
    private static final String ADMIN_VIEW = "admin";

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping(value = "makeCompany")
    public ResponseEntity makeCompany(@Valid CompanyDto companyDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getFieldErrors().get(0).getDefaultMessage());
        }
        try {
            companyService.createCompany(companyDto.getCompanyName());
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body(String.format(COMPANY_IS_ALREADY_EXIST, companyDto.getCompanyName()));
        }
        return ResponseEntity.ok(String.format(COMPANY_WAS_CREATED, companyDto.getCompanyName()));
    }

    @GetMapping(value = "company")
    public ModelAndView showAllCompanies() {
        return new ModelAndView(ADMIN_VIEW);
    }

    @GetMapping(value = "companies")
    public ResponseEntity<Page<Company>> showAllCompanies(int page) {
        logger.debug("show companies from page=" + page);
        return ResponseEntity.ok(companyService.findAll(new PageRequest(page - 1, NUMBER_OF_PAGES)));
    }

    @PostMapping(value = "removeCompany")
    public ResponseEntity removeCompany(@RequestParam String companyName) {
        logger.debug("removeCompany " + companyName);
        companyService.deleteCompanyByName(companyName);
        return ResponseEntity.ok(String.format(COMPANY_WAS_DELETED, companyName));
    }

    @GetMapping(value = "getAllCompany")
    public ResponseEntity<Iterable<Company>> getAllCompany() {
        logger.debug("get all company");
        return ResponseEntity.ok(companyService.findAll());
    }

    @RequestMapping(value = "editCompany")
    public ResponseEntity makeCompany(@RequestParam String preCompanyName,@RequestParam String companyName) {
        Company company = companyService.findByName(preCompanyName);
        company.setName(companyName);
        companyService.editCompany(company);
        return ResponseEntity.ok(null);
    }
}