package ua.softgroup.medreview.web.dto;

import org.hibernate.validator.constraints.NotEmpty;
import ua.softgroup.medreview.persistent.entity.Role;

import javax.validation.constraints.Size;

/**
 * @author Sergiy Perevyazko <sg.sergiyp@gmail.com>
 */
public class UserDto {

    private String preLogin;
    @NotEmpty(message = "Username cannot be empty")
    @Size(max = 32, message = "Username is too long (maximum is 32 characters)")
    private String login;
    private String password;
    private Role role;
    private String company;
    private String language;

    public UserDto() {
    }

    public UserDto(String preLogin, String login, String password, Role role, String company, String language) {
        this.preLogin = preLogin;
        this.login = login;
        this.password = password;
        this.role = role;
        this.company = company;
        this.language = language;
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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "preLogin='" + preLogin + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", company='" + company + '\'' +
                '}';
    }
}
