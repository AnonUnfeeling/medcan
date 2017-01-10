package ua.softgroup.medreview.web.dto;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

/**
 * @author Sergiy Perevyazko <sg.sergiyp@gmail.com>
 */
public class TreatmentDto {

    @NotEmpty(message = "Name can not be empty")
    @Size(max = 64, message = "Name is too long (maximum is 64 characters)")
    private String name;
    @NotEmpty(message = "Subcategory can not be empty")
    private String subSubject;

    public TreatmentDto() {
    }

    public TreatmentDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubSubject() {
        return subSubject;
    }

    public void setSubSubject(String subSubject) {
        this.subSubject = subSubject;
    }

    @Override
    public String toString() {
        return "TreatmentDto{" +
                "name='" + name + '\'' +
                ", subSubject='" + subSubject + '\'' +
                '}';
    }
}
