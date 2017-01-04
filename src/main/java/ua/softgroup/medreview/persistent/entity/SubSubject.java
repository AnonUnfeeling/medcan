package ua.softgroup.medreview.persistent.entity;

import javax.persistence.*;

/**
 * @author Sergiy Perevyazko <sg.sergiyp@gmail.com>
 * @author Oleksandr Tyshkovets <sg.olexander@gmail.com>
 */
@Entity
public class SubSubject extends AbstractEntity<Long> {
    private static final long serialVersionUID = 98000298364542854L;

    @Column
    private String name;

    @ManyToOne
    private Subject subject;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }
}
