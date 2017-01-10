package ua.softgroup.medreview.persistent.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Sergiy Perevyazko <sg.sergiyp@gmail.com>
 * @author Oleksandr Tyshkovets <sg.olexander@gmail.com>
 */
@Entity
public class SubSubject extends AbstractEntity<Long> {
    private static final long serialVersionUID = 98000298364542854L;

    @Column
    private String name;

    @JsonIgnore
    @ManyToOne
    private Subject subject;

    @OneToMany(mappedBy = "subSubject", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Treatment> treatments = new ArrayList<>();

    public SubSubject() {
    }

    public SubSubject(String name, Subject subject) {
        this.name = name;
        this.subject = subject;
    }

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

    public List<Treatment> getTreatments() {
        return treatments;
    }

    public void setTreatments(List<Treatment> treatments) {
        this.treatments.clear();
        if (treatments != null) {
            this.treatments = treatments;
        }
    }
}
