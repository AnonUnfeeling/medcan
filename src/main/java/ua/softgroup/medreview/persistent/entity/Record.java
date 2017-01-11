package ua.softgroup.medreview.persistent.entity;

import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.snowball.SnowballPorterFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.AnalyzerDef;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Parameter;
import org.hibernate.search.annotations.Store;
import org.hibernate.search.annotations.TokenFilterDef;
import org.hibernate.search.annotations.TokenizerDef;

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
@AnalyzerDef(name = "en",
        tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class),
        filters = {
                @TokenFilterDef(factory = LowerCaseFilterFactory.class),
                @TokenFilterDef(factory = SnowballPorterFilterFactory.class, params = {
                        @Parameter(name = "language", value = "English")
                })
        })
public class Record extends AbstractEntity<Long> {
    private static final long serialVersionUID = 5768370930485023928L;

    @Column(nullable = false, unique = true, length = 500)
    @Field(analyzer = @Analyzer(definition = "en"))
    private String title;

    @Column(columnDefinition = "text")
    private String endDescription;

    @Column(columnDefinition = "text")
    private String endConclusion;

    @Column
    private String status;

    @Column
    private String country;

    @Column
    private String url;

    @Column
    private String type = RecordType.BOOK.getType();

    @Column
    @CreationTimestamp
    @Field(store = Store.YES, index = Index.YES, analyze = Analyze.NO)
    private LocalDateTime creationDate;

    @ManyToOne
    @IndexedEmbedded
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Record{" +
                "title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", endConclusion='" + endConclusion + '\'' +
                ", endDescription='" + endDescription + '\'' +
                ", country='" + country + '\'' +
                ", status='" + status + '\'' +
                ", creationDate=" + creationDate +
                ", author=" + author +
                '}';
    }
}
