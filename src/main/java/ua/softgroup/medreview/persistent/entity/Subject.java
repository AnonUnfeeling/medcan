package ua.softgroup.medreview.persistent.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Sergiy Perevyazko <sg.sergiyp@gmail.com>
 */
@Entity
public class Subject extends AbstractEntity<Long> {
    private static final long serialVersionUID = 7319584613784286792L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubSubject> subSubjects = new ArrayList<>();

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
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
            subSubjects.forEach(subSubject -> subSubject.setSubject(this));
            this.subSubjects.addAll(subSubjects);
        }
    }
}
