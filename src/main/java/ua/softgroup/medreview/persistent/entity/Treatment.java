package ua.softgroup.medreview.persistent.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * @author Sergiy Perevyazko <sg.sergiyp@gmail.com>
 */
@Entity
public class Treatment extends AbstractEntity<Long> {
    private static final long serialVersionUID = -531834217433114165L;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToOne
    private SubSubject subSubject;

    public Treatment() {
    }

    public Treatment(String name, SubSubject subSubject) {
        this.name = name;
        this.subSubject = subSubject;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SubSubject getSubSubject() {
        return subSubject;
    }

    public void setSubSubject(SubSubject subSubject) {
        this.subSubject = subSubject;
    }
}
