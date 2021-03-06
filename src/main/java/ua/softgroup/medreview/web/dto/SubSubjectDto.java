package ua.softgroup.medreview.web.dto;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

/**
 * @author Sergiy Perevyazko <sg.sergiyp@gmail.com>
 */
public class SubSubjectDto {

    @NotEmpty(message = "Name can not be empty")
    @Size(max = 64, message = "Name is too long (maximum is 64 characters)")
    private String name;
    private String oldName;
    private String subject;

    public SubSubjectDto() {
    }

    public SubSubjectDto(String name) {
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public String toString() {
        return "SubSubjectDto{" +
                "name='" + name + '\'' +
                ", oldName='" + oldName + '\'' +
                ", subject='" + subject + '\'' +
                '}';
    }
}
