package ua.softgroup.medreview.persistent.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

/**
 * @author Oleksandr Tyshkovets <sg.olexander@gmail.com>
 */
@Entity
@Indexed
public class Note extends AbstractEntity<Long> {
    private static final long serialVersionUID = 6627846745698054192L;

    @Column
    @Field(analyzer = @Analyzer(definition = "en"))
    private String description;

    @Column
    @Field(analyzer = @Analyzer(definition = "en"))
    private String conclusion;

    @Column
    @Field(analyzer = @Analyzer(definition = "en"))
    private String keywords;

    @Column
    @Field(analyzer = @Analyzer(definition = "en"))
    private String subject;

    @Column
    @Field(analyzer = @Analyzer(definition = "en"))
    private String subSubject;

    @Column
    @Field(analyzer = @Analyzer(definition = "en"))
    private String country;

    @Column
    @Field(analyzer = @Analyzer(definition = "en"))
    private String language;

    @Column
    @Field(analyzer = @Analyzer(definition = "en"))
    private String status;

    @JsonIgnore
    @Column
    @CreationTimestamp
    @Field(analyze = Analyze.NO, store = Store.YES)
    private LocalDateTime creationDate;

    @Column
    @UpdateTimestamp
    private LocalDateTime updateDate;

    @JsonIgnore
    @ManyToOne
    private Record record;

    public Note() {
    }

    public Note(String description, String conclusion, String keywords, String subject, String subSubject,
                String country, String language, String status) {
        this.description = description;
        this.conclusion = conclusion;
        this.keywords = keywords;
        this.subject = subject;
        this.subSubject = subSubject;
        this.country = country;
        this.language = language;
        this.status = status;
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubSubject() {
        return subSubject;
    }

    public void setSubSubject(String subSubject) {
        this.subSubject = subSubject;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
    }

    @Override
    public String toString() {
        return "Note{" +
                "description='" + description + '\'' +
                ", conclusion='" + conclusion + '\'' +
                ", keywords='" + keywords + '\'' +
                ", subject='" + subject + '\'' +
                ", subSubject='" + subSubject + '\'' +
                ", country='" + country + '\'' +
                ", language='" + language + '\'' +
                ", status='" + status + '\'' +
                ", creationDate=" + creationDate +
                ", updateDate=" + updateDate +
                '}';
    }
}
