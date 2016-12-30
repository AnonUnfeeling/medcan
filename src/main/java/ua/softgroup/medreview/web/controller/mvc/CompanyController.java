package ua.softgroup.medreview.web.controller.mvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import ua.softgroup.medreview.persistent.entity.Company;
import ua.softgroup.medreview.persistent.entity.Record;
import ua.softgroup.medreview.persistent.repository.CompanyRepository;

import java.util.Arrays;
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
    ResponseEntity makeCompany(@RequestParam String companyName) {
        if (companyName.isEmpty()) {
            logger.log(Level.SEVERE, "error by create company name is empty");
            return new ResponseEntity(Keys.EMPTY.toString(), HttpStatus.OK);
        } else if (isCreate(companyName)) {
            return ResponseEntity.ok(HttpStatus.OK);
        } else {
            return new ResponseEntity(Keys.FAIL.toString(), HttpStatus.OK);
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
    @RequestMapping(value = "company", method = RequestMethod.GET)
    public ModelAndView showAllCompanies() {
        logger.log(Level.INFO, "get all companies");
        return new ModelAndView("admin");
    }

    //TODO: this link for admin
    @RequestMapping(value = "companies", method = RequestMethod.GET)
    public
    @ResponseBody
    String showAllCompanies(int page) {
        logger.log(Level.INFO, "get all companies");
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(new JSONModel(Arrays.asList(companyRepository.findAll()).size() / 10, companyRepository.findAll(new PageRequest(page - 1, 10))));
        } catch (JsonProcessingException e) {
            logger.log(Level.SEVERE, "error by create json");
            return "";
        }
    }

    //TODO: this link for admin
    @RequestMapping(value = "removeCompany", method = RequestMethod.POST)
    public
    @ResponseBody
    ResponseEntity removeCompany(@RequestParam String companyName) {
        if (companyName.isEmpty()) {
            logger.log(Level.SEVERE, "error by create company name is empty");
            return new ResponseEntity(Keys.EMPTY.toString(), HttpStatus.OK);
        } else if (isDeleted(companyName)) {
            return ResponseEntity.ok(HttpStatus.OK);
        } else {
            return new ResponseEntity(Keys.FAIL.toString(), HttpStatus.OK);
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