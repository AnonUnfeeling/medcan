package ua.softgroup.medreview.web.form;

import org.hibernate.validator.constraints.NotEmpty;
import ua.softgroup.medreview.persistent.entity.User;

import javax.validation.constraints.Size;

/**
 * Created by jdroidcoder on 03.01.2017.
 */
public class UserForm {
    @NotEmpty(message = "Username cannot be empty")
    @Size(max = 32, message = "Username is too long (maximum is 32 characters)")
    private String login;
    @NotEmpty(message = "Password cannot be empty")
    @Size(max = 32, message = "Username is too long (maximum is 32 characters)")
    private String password;
    @NotEmpty(message = "Role cannot be empty")
    private String role;
    private String company;

    public UserForm() {
    }

    public UserForm(String login, String password, String role, String company) {
        this.login = login;
        this.password = password;
        this.role = role;
        this.company = company;
    }

    public UserForm(User user) {
        this.login = user.getLogin();
        this.role = user.getRoles().get(0).getRole().toString();
        try {
            this.company = user.getCompany().getName();
        } catch (Exception e) {
        }
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    @Override
    public String toString() {
        return "UserForm{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", company='" + company + '\'' +
                '}';
    }
}
