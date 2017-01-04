package ua.softgroup.medreview.web.controller.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by jdroidcoder on 29.12.2016.
 */
@Controller
public class MainController {
    @RequestMapping(value = "userLogin")
    public String loginUser() {
        return "redirect:records";
    }

    @RequestMapping(value = "companyLogin")
    public String loginCompany() {
        return "redirect:users";
    }

    @RequestMapping(value = "adminLogin")
    public String loginAdmin() {
        return "redirect:company";
    }
}
