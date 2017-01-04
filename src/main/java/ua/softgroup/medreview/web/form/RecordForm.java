package ua.softgroup.medreview.web.form;

import org.hibernate.validator.constraints.NotEmpty;

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

}
