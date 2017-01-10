package ua.softgroup.medreview.web.dto;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

/**
 * @author Sergiy Perevyazko <sg.sergiyp@gmail.com>
 */
public class SubjectDto {
    @NotEmpty(message = "Name can not be empty")
    @Size(max = 64, message = "Name is too long (maximum is 64 characters)")
    private String name;

    private String oldName;

    public SubjectDto() {
    }

    public SubjectDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOldName() {
        return oldName;
    }

    public void setOldName(String oldName) {
        this.oldName = oldName;
    }

    @Override
    public String toString() {
        return "SubjectDto{" +
                "name='" + name + '\'' +
                ", oldName='" + oldName + '\'' +
                '}';
    }
}
