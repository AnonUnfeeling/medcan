package ua.softgroup.medreview.web.controller.mvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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
import ua.softgroup.medreview.web.dto.UserDto;
import ua.softgroup.medreview.web.form.UserForm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import ua.softgroup.medreview.service.CompanyService;
import ua.softgroup.medreview.service.UserService;

import javax.validation.Valid;

/**
 * Created by jdroidcoder on 28.12.2016.
 */
@RestController
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final int NUMBER_OF_PAGES = 10;

    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private UserService userService;
    @Autowired
    private CompanyService companyservice;

    @PostMapping("/checkRole")
    public
    @ResponseBody
    String checkrole() {
        return authenticationService.getPrincipal().getRoles().get(0).getRole().toString();
    }

    @PostMapping("/checkCompany")
    public
    @ResponseBody
    String checkCompany() {
        return authenticationService.getPrincipal().getCompany().getName();
    }

    @PostMapping("/checkUser")
    public
    @ResponseBody
    String checkUser() {
        return authenticationService.getPrincipal().getRoles().get(0).getUser().getLogin();
    }

    @PostMapping(value = "user")
    public ResponseEntity createUser(@Valid UserForm userForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getFieldErrors().get(0).getDefaultMessage());
        }
        try {
            userService.createUser(userForm);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body(String.format("Username \"%s\" is already taken.", userForm.getLogin()));
        }
        return ResponseEntity.ok(String.format("User \"$s\" was created successfully.", userForm.getLogin()));
    }

    @RequestMapping(value = "user/edit", method = RequestMethod.POST)
    public ResponseEntity editUser(@Valid UserDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getFieldErrors().get(0).getDefaultMessage());
        }
        userService.updateUser(userDto);
        return ResponseEntity.ok("User was changed successfully.");
    }

    @RequestMapping(value = "usersByCompany", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Page<User>> showUsersByCompany(@RequestParam String companyName, @RequestParam int page) {
        return ResponseEntity.ok(userService.getUsersByCompany(companyservice.findByName(companyName), new PageRequest(page - 1, NUMBER_OF_PAGES)));
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
            Page<User> userPage = userService.findAll(new PageRequest(page - 1, NUMBER_OF_PAGES));
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
        return userService.findAll(new PageRequest(0, NUMBER_OF_PAGES)).getTotalPages();
    }
}
