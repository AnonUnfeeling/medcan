package ua.softgroup.medreview.web.controller.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ua.softgroup.medreview.persistent.entity.Company;
import ua.softgroup.medreview.persistent.repository.CompanyRepository;

/**
 * Created by jdroidcoder on 28.12.2016.
 */
@Controller
public class CompanyController {

    @Autowired
    CompanyRepository companyRepository;

    @RequestMapping(value = "makeCompany", method = RequestMethod.GET)
    public String makeCompany(String companyName){
        try {
            companyRepository.save(new Company(companyName));
        }catch (Exception e){
            System.out.println("company is present");
        }
        return "";
    }
}
