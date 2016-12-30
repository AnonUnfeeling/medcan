package ua.softgroup.medreview.web.controller.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by jdroidcoder on 29.12.2016.
 */
@Controller
public class MainController {
    @RequestMapping(value = "user")
    public String loginUser() {
        return "redirect:records";
    }

    @RequestMapping(value = "company")
    public String loginCompany() {
        return "redirect:records";
    }

    @RequestMapping(value = "admin")
    public String loginAdmin() {
        return "redirect:companies";
    }
}
