package ua.softgroup.medreview.persistent.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Oleksandr Tyshkovets <sg.olexander@gmail.com>
 */
@Entity
@Indexed
public class Record extends AbstractEntity<Long> {
    private static final long serialVersionUID = 5768370930485023928L;

    @Column(nullable = false, unique = true)
    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    private String title;

    @Column
    private String type;

    @Column
    @CreationTimestamp
    @Field(store = Store.YES, index = Index.YES, analyze = Analyze.NO)
    private LocalDateTime creationDate;

    @ManyToOne
    private User author;

    @OneToMany(mappedBy = "record", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Note> notes = new ArrayList<>();

    public Record() {
    }

    public Record(String title, String type, User author) {
        this.title = title;
        this.type = type;
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "Record{" +
                "title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", creationDate=" + creationDate +
                ", author=" + author +
                '}';
    }
}
