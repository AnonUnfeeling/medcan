package ua.softgroup.medreview.persistent.entity;

import javax.persistence.*;

/**
 * Created by Sergiy Perevyazko on 28.12.2016.
 */
@Entity
public class UserRole extends AbstractEntity<Long> {

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne
    private User user;

    public UserRole() {
    }

    public UserRole(Role role, User user) {
        this.role = role;
        this.user = user;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return role.toString();
    }
}
