package ua.softgroup.medreview.persistent.entity;

/**
 * @author Oleksandr Tyshkovets <sg.olexander@gmail.com>
 */
public enum RecordType {
    BOOK("Book"), WEBSITE("Website"), DOCTOR("Doctor");

    private String type;

    RecordType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
