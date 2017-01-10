package ua.softgroup.medreview.web.form;

import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.validation.constraints.Size;

/**
 * @author Oleksandr Tyshkovets <sg.olexander@gmail.com>
 */
public class RecordForm {

    @NotEmpty(message = "Title cannot be empty")
    @Size(min = 6, max = 60, message = "Title must between 6 and 60 characters")
    private String title;
    @NotEmpty(message = "Type cannot be empty")
    private String type;
    private String endDescription;
    private String endConclusion;
    private String status;
    private String country;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEndDescription() {
        return endDescription;
    }

    public void setEndDescription(String endDescription) {
        this.endDescription = endDescription;
    }

    public String getEndConclusion() {
        return endConclusion;
    }

    public void setEndConclusion(String endConclusion) {
        this.endConclusion = endConclusion;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "RecordForm{" +
                "title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", endDescription='" + endDescription + '\'' +
                ", endConclusion='" + endConclusion + '\'' +
                ", status='" + status + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
