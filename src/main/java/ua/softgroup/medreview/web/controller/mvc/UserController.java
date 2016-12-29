package ua.softgroup.medreview.web.controller.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ua.softgroup.medreview.persistent.entity.Role;
import ua.softgroup.medreview.persistent.entity.User;
import ua.softgroup.medreview.persistent.repository.CompanyRepository;
import ua.softgroup.medreview.persistent.repository.UserRepository;

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

    @RequestMapping(value = "user", method = RequestMethod.POST)
    public String makeUser(User user) {
        try {
            userRepository.save(user);
            logger.log(Level.SEVERE, "create new user");
            return Keys.CREATED.toString();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "error by create user", e);
            return Keys.FAIL.toString();
        }
    }

    @RequestMapping(value = "users", method = RequestMethod.POST)
    public ModelAndView showUserByCompany(String companyName) {
        return new ModelAndView("", "users", companyRepository.findByName(companyName).getUsers());
    }

    @RequestMapping(value = "users", method = RequestMethod.GET)
    public ModelAndView showUser() {
        return new ModelAndView("", "users", userRepository.findAll());
    }
}
