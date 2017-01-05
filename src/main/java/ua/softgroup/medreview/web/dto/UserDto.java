package ua.softgroup.medreview.web.dto;

import ua.softgroup.medreview.persistent.entity.Role;

/**
 * @author Sergiy Perevyazko <sg.sergiyp@gmail.com>
 */
public class UserDto {

    private String preLogin;
    private String login;
    private String password;
    private Role role;
    private String company;

    public UserDto() {
    }

    public UserDto(String preLogin, String login, String password, Role role, String company) {
        this.preLogin = preLogin;
        this.login = login;
        this.password = password;
        this.role = role;
        this.company = company;
    }

    public String getPreLogin() {
        return preLogin;
    }

    public void setPreLogin(String preLogin) {
        this.preLogin = preLogin;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}