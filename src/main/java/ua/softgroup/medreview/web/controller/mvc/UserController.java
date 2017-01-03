package ua.softgroup.medreview.web.controller.mvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ua.softgroup.medreview.persistent.entity.Role;
import ua.softgroup.medreview.persistent.entity.User;

import ua.softgroup.medreview.persistent.entity.UserRole;
import ua.softgroup.medreview.service.AuthenticationService;
import ua.softgroup.medreview.web.form.UserForm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import ua.softgroup.medreview.service.CompanyService;
import ua.softgroup.medreview.service.UserService;

/**
 * Created by jdroidcoder on 28.12.2016.
 */
@RestController
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private UserService userService;
    @Autowired
    private CompanyService companyservice;


    //TODO: this link for admin and company role
    @RequestMapping(value = "user", method = RequestMethod.POST)
    public
    @ResponseBody
    ResponseEntity
    makeUser(@RequestParam("login") String login,
             @RequestParam("password") String password,
             @RequestParam("role") String role,
             @RequestParam("company") String company) {
        try {
            User user = new User();
            user.setLogin(login);
            user.setPassword(password);
            List<UserRole> roles = new ArrayList<>();
            if (Role.ADMIN.toString().equals(role)) {
                roles.add(new UserRole(Role.ADMIN, user));
                roles.add(new UserRole(Role.COMPANY, user));
            } else if (Role.COMPANY.toString().equals(role)) {
                roles.add(new UserRole(Role.COMPANY, user));
            } else {
                roles.add(new UserRole(Role.USER, user));
            }
            user.setRoles(roles);
            user.setCompany(companyservice.findByName(company));
            userService.saveUser(user);

            return ResponseEntity.ok(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(Keys.FAIL.toString(), HttpStatus.OK);
        }
    }

    //TODO: this link for admin and company role
    @RequestMapping(value = "usersByCompany", method = RequestMethod.GET)
    public
    @ResponseBody
    String showUserByCompany(String companyName, int page) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<UserForm> userForms = new ArrayList<>();
            Page<User> userPage = userService.findAll(new PageRequest(page - 1, 10));
            for (User user : userPage) {
                if (authenticationService.getPrincipal().getRoles().get(0).getRole().equals(Role.ADMIN)) {
                    try {
                        if (user.getCompany().getName().equals(companyName)) {
                            userForms.add(new UserForm(user));
                        }
                    } catch (Exception e) {

                    }
                }
            }
            return mapper.writeValueAsString(userForms);
        } catch (JsonProcessingException e) {
            return "";
        }
    }


    @RequestMapping(value = "users", method = RequestMethod.GET)
    public ModelAndView showUser() {
        return new ModelAndView("users");
    }

    @RequestMapping(value = "user", method = RequestMethod.GET)
    public ModelAndView showUser(String companyName, int page) {
        Map<String, String> map = new TreeMap<>();
        map.put(companyName, companyName);
        map.put("" + page, "" + page);
        return new ModelAndView("users", map);
    }

    @RequestMapping(value = "users", method = RequestMethod.POST)
    public
    @ResponseBody
    String showUserByJS(int page) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<UserForm> userForms = new ArrayList<>();
            Page<User> userPage = userService.findAll(new PageRequest(page - 1, 10));
            for (User user : userPage) {
                if (authenticationService.getPrincipal().getRoles().get(0).getRole().equals(Role.ADMIN)) {
                    userForms.add(new UserForm(user));
                } else if (authenticationService.getPrincipal().getRoles().get(0).getRole().equals(Role.COMPANY)) {
                    try {
                        if (authenticationService.getPrincipal().getCompany().getName().equals(user.getCompany().getName())) {
                            userForms.add(new UserForm(user));
                        }
                    } catch (Exception e) {

                    }
                }
            }
            return mapper.writeValueAsString(userForms);
        } catch (JsonProcessingException e) {
            return "";
        }
    }

    @RequestMapping(value = "getRoles")
    public
    @ResponseBody
    List<String> getAllRoles() {
        List<String> roles = new ArrayList<>();
        roles.add(Role.ADMIN.toString());
        roles.add(Role.COMPANY.toString());
        roles.add(Role.USER.toString());
        return roles;
    }

    @PostMapping(value = "removeUser")
    public ResponseEntity removeUser(@RequestParam String userLogin) {
        logger.debug("removeUser " + userLogin);
        try {
            userService.deleteUserByLogin(userLogin);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(null);
        }
        return ResponseEntity.ok(null);
    }

    @GetMapping(value = "countPageUsers")
    private
    @ResponseBody
    int countPageUsers() {
        return userService.findAll(new PageRequest(0, 10)).getTotalPages();
    }
}
