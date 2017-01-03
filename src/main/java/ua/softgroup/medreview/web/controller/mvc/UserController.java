package ua.softgroup.medreview.web.controller.mvc;

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
import org.springframework.web.servlet.ModelAndView;
import ua.softgroup.medreview.persistent.entity.Company;
import ua.softgroup.medreview.persistent.entity.Role;
import ua.softgroup.medreview.persistent.entity.User;
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

/**
 * Created by jdroidcoder on 28.12.2016.
 */
@Controller
public class UserController {
    private Logger logger = Logger.getLogger(UserController.class.getName());

    @Autowired
    UserRepository userRepository;

    @Autowired
    CompanyRepository companyRepository;

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
        }
    }

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
    }
}
