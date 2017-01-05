package ua.softgroup.medreview.web.controller.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.softgroup.medreview.service.AuthenticationService;

/**
 * Created by jdroidcoder on 29.12.2016.
 */
@Controller
@RequestMapping("/")
public class MainController {

    @Autowired
    private AuthenticationService authenticationService;

    @RequestMapping
    public String homePage() {
//        return "redirect:" + authenticationService.determineHomeUrlByRole();
        return "redirect:/records";
    }
}
