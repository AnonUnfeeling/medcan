package ua.softgroup.medreview.web.dto;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

/**
 * @author Sergiy Perevyazko <sg.sergiyp@gmail.com>
 */
public class NoteDto {

    private long id;
    @NotEmpty(message = "Title can not be empty")
    @Size(max = 64, message = "Title is too long (maximum is 64 characters)")
    private String title;
    private String description;
    private String conclusion;
    private String keywords;
    private String category;
    private String subCategory;
    private String treatment;
    private String recordTitle;

    public NoteDto() {
    }

    public NoteDto(long id, String title, String description, String conclusion, String keywords, String category, String subCategory, String treatment, String recordTitle) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.conclusion = conclusion;
        this.keywords = keywords;
        this.category = category;
        this.subCategory = subCategory;
        this.treatment = treatment;
        this.recordTitle = recordTitle;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getConclusion() {
        return conclusion;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public String getRecordTitle() {
        return recordTitle;
    }

    public void setRecordTitle(String recordTitle) {
        this.recordTitle = recordTitle;
    }

    @Override
    public String toString() {
        return "NoteDto{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", conclusion='" + conclusion + '\'' +
                ", keywords='" + keywords + '\'' +
                ", category='" + category + '\'' +
                ", subCategory='" + subCategory + '\'' +
                ", treatment='" + treatment + '\'' +
                ", recordTitle='" + recordTitle + '\'' +
                '}';
    }
}
