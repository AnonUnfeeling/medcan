package ua.softgroup.medreview.web.controller.mvc;

<<<<<<< HEAD
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
=======
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
>>>>>>> 8d9cb6a3be36ac7847ee7264a32bde5c8b105197
import org.springframework.web.servlet.ModelAndView;
import ua.softgroup.medreview.persistent.entity.Company;
import ua.softgroup.medreview.persistent.entity.Role;
import ua.softgroup.medreview.persistent.entity.User;
<<<<<<< HEAD
import ua.softgroup.medreview.persistent.entity.UserRole;
import ua.softgroup.medreview.persistent.repository.CompanyRepository;
import ua.softgroup.medreview.persistent.repository.UserRepository;
import ua.softgroup.medreview.web.form.UserForm;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
=======
import ua.softgroup.medreview.service.CompanyService;
import ua.softgroup.medreview.service.UserService;
>>>>>>> 8d9cb6a3be36ac7847ee7264a32bde5c8b105197

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

<<<<<<< HEAD
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
            if(Role.ADMIN.toString().equals(role)){
                roles.add(new UserRole(Role.ADMIN,user));
                roles.add(new UserRole(Role.COMPANY,user));
            }else if(Role.COMPANY.equals(role)){
                roles.add(new UserRole(Role.COMPANY,user));
            }else {
                roles.add(new UserRole(Role.USER,user));
            }
            user.setRoles(roles);
            user.setCompany(companyRepository.findByName(company));
            userRepository.save(user);
            logger.log(Level.SEVERE, "create new user");
            return ResponseEntity.ok(HttpStatus.OK);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "error by create user", e);
            return new ResponseEntity(Keys.FAIL.toString(), HttpStatus.OK);
=======
    @PostMapping(value = "user")
    public ResponseEntity createUser(@RequestBody User user) {
        try {
            userService.saveUser(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(null);
>>>>>>> 8d9cb6a3be36ac7847ee7264a32bde5c8b105197
        }
        return ResponseEntity.ok(String.format("User '%s' has been created.", user.getUsername()));
    }

<<<<<<< HEAD
    //TODO: this link for admin and company role
    @RequestMapping(value = "usersByCompany", method = RequestMethod.POST)
    public ModelAndView showUserByCompany(String companyName) {
        return new ModelAndView("users", "users", companyRepository.findByName(companyName).getUsers());
    }


    @RequestMapping(value = "users", method = RequestMethod.GET)
    public ModelAndView showUser() {
        return new ModelAndView("users", "users", userRepository.findAll());
    }

    @RequestMapping(value = "users", method = RequestMethod.POST)
    public
    @ResponseBody
    String showUserByJS(int page) {
        logger.log(Level.INFO, "get users page = " + page);
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<UserForm> userForms = new ArrayList<>();
            Page<User> userPage = userRepository.findAll(new PageRequest(page - 1, 10));
            for (User user : userPage) {
                userForms.add(new UserForm(user));
            }
            return mapper.writeValueAsString(userForms);
        } catch (JsonProcessingException e) {
            logger.log(Level.SEVERE, "error by get user json", e);
            return "";
        }
    }

    @RequestMapping(value = "getRoles")
    public
    @ResponseBody
    List<String> getAllRoles() {
        logger.log(Level.INFO, "get all roles");
        List<String> roles = new ArrayList<>();
        roles.add(Role.ADMIN.toString());
        roles.add(Role.COMPANY.toString());
        roles.add(Role.USER.toString());
        return roles;
=======
    @PostMapping(value = "users")
    public ModelAndView showUserByCompany(String companyName) {
        return new ModelAndView("", "users", companyservice.findByName(companyName).getUsers());
    }

    @GetMapping(value = "users")
    public ModelAndView showUser() {
        return new ModelAndView("", "users", userService.findAllUsers());
>>>>>>> 8d9cb6a3be36ac7847ee7264a32bde5c8b105197
    }
}
