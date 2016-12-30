package ua.softgroup.medreview.web.controller.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import ua.softgroup.medreview.persistent.entity.Company;
import ua.softgroup.medreview.persistent.repository.CompanyRepository;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by jdroidcoder on 28.12.2016.
 */
@Controller
public class CompanyController {
    private Logger logger = Logger.getLogger(CompanyController.class.getName());

    @Autowired
    CompanyRepository companyRepository;

    //TODO: this link for admin
    @RequestMapping(value = "makeCompany", method = RequestMethod.POST)
    public
    @ResponseBody
    String makeCompany(@RequestParam String companyName) {
        if (companyName.isEmpty()) {
            logger.log(Level.SEVERE, "error by create company name is empty");
            return Keys.EMPTY.toString();
        } else if (isCreate(companyName)) {
            return Keys.CREATED.toString();
        } else {
            return Keys.FAIL.toString();
        }
    }

    private boolean isCreate(String companyName) {
        try {
            companyRepository.save(new Company(companyName));
            logger.log(Level.INFO, "create new company");
            return true;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "create new company - error company is present");
            return false;
        }
    }

    //TODO: this link for admin
    @RequestMapping(value = "companies", method = RequestMethod.GET)
    public ModelAndView showAllCompanies() {
        logger.log(Level.INFO, "get all companies");
        return new ModelAndView("admin", "companies", companyRepository.findAll());
    }

    //TODO: this link for admin
    @RequestMapping(value = "removeCompany", method = RequestMethod.POST)
    public String removeCompany(String companyName) {
        if (companyName.isEmpty()) {
            logger.log(Level.SEVERE, "error by create company name is empty");
            return Keys.EMPTY.toString();
        } else if (isDeleted(companyName)) {
            return Keys.DELETED.toString();
        } else {
            return Keys.FAIL.toString();
        }
    }

    private boolean isDeleted(String companyName) {
        try {
            companyRepository.delete(companyRepository.findByName(companyName));
            logger.log(Level.SEVERE, "remove company");
            return true;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "error by remove company", e);
            return false;
        }
    }
}