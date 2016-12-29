package ua.softgroup.medreview.web.controller.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ua.softgroup.medreview.persistent.entity.Role;
import ua.softgroup.medreview.persistent.entity.User;
import ua.softgroup.medreview.persistent.repository.CompanyRepository;
import ua.softgroup.medreview.persistent.repository.UserRepository;

/**
 * Created by jdroidcoder on 28.12.2016.
 */
@Controller
public class UserController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    CompanyRepository companyRepository;

    @RequestMapping(value = "makeUser", method = RequestMethod.GET)
    private String makeUser() {
        User user = new User("dsa2", "222", Role.ADMIN, companyRepository.findByName("123"));
        try {
            userRepository.save(user);
        }catch (Exception e){
            System.out.println("user is present");
        }
        return "";
    }

//    @RequestMapping(value = "findUser", method = RequestMethod.GET)
//    private User findUserByName(String userName){
//        return userRepository.findByUsername(userName);
//    }
}
