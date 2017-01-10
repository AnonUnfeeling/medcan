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
public class Subject extends AbstractEntity<Long> {
    private static final long serialVersionUID = 7319584613784286792L;

    @Column
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubSubject> subSubjects = new ArrayList<>();

    public Subject() {
    }

    public Subject(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SubSubject> getSubSubjects() {
        return subSubjects;
    }

    public void setSubSubjects(List<SubSubject> subSubjects) {
        this.subSubjects.clear();
        if (subSubjects != null) {
//            subSubjects.forEach(subSubject -> subSubject.setSubject(this)); //we don't need SubSubjects without subject inside in our case
            this.subSubjects.addAll(subSubjects);
        }
    }
}
