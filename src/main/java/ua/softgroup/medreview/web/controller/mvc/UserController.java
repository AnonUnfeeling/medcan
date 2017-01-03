package ua.softgroup.medreview.web.controller.mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ua.softgroup.medreview.persistent.entity.User;
import ua.softgroup.medreview.service.CompanyService;
import ua.softgroup.medreview.service.UserService;

/**
 * Created by jdroidcoder on 28.12.2016.
 */
@RestController
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;
    @Autowired
    private CompanyService companyservice;

    @PostMapping(value = "user")
    public ResponseEntity createUser(@RequestBody User user) {
        try {
            userService.saveUser(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(null);
        }
        return ResponseEntity.ok(String.format("User '%s' has been created.", user.getUsername()));
    }

    @PostMapping(value = "users")
    public ModelAndView showUserByCompany(String companyName) {
        return new ModelAndView("", "users", companyservice.findByName(companyName).getUsers());
    }

    @GetMapping(value = "users")
    public ModelAndView showUser() {
        return new ModelAndView("", "users", userService.findAllUsers());
    }
}
