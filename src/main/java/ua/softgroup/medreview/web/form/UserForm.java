package ua.softgroup.medreview.web.form;

import ua.softgroup.medreview.persistent.entity.User;

/**
 * Created by jdroidcoder on 03.01.2017.
 */
public class UserForm {
    private String login;
    private String role;
    private String company;

    public UserForm(String login, String role, String company) {
        this.login = login;
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
}
