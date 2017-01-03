package ua.softgroup.medreview.web.controller.mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
<<<<<<< HEAD
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
=======
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
>>>>>>> 8d9cb6a3be36ac7847ee7264a32bde5c8b105197
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
<<<<<<< HEAD
import ua.softgroup.medreview.persistent.entity.Company;
import ua.softgroup.medreview.persistent.repository.CompanyRepository;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by jdroidcoder on 28.12.2016.
 */
@Controller
=======
import ua.softgroup.medreview.persistent.entity.Record;
import ua.softgroup.medreview.service.CompanyService;

@RestController
>>>>>>> 8d9cb6a3be36ac7847ee7264a32bde5c8b105197
public class CompanyController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CompanyService companyService;

    @PostMapping(value = "makeCompany")
    public ResponseEntity makeCompany(@RequestParam String companyName) {
        logger.debug("makeCompany");
        try {
            companyService.createCompany(companyName);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(String.format("Company '%s' has not been created.", companyName));
        }
        return ResponseEntity.ok(null);
    }

    @GetMapping(value = "company")
    public ModelAndView showAllCompanies() {
        return new ModelAndView("admin");
    }

    @GetMapping(value = "companies")
    public ResponseEntity<Page<Record>> showAllCompanies(int page) {
        logger.debug("show companies from page=" + page);
        return ResponseEntity.ok(companyService.findAll(new PageRequest(page - 1, 10)));
    }

    @PostMapping(value = "removeCompany")
    public ResponseEntity removeCompany(@RequestParam String companyName) {
        logger.debug("removeCompany " + companyName);
        try {
            companyService.deleteCompanyByName(companyName);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(null);
        }
        return ResponseEntity.ok(null);
    }

    @RequestMapping(value = "getAllCompany")
    public
    @ResponseBody
    Iterable<Company> getAllCompany() {
        logger.log(Level.INFO, "get all company");
        return companyRepository.findAll();
    }
}