package com.disertatie.mylibrary.domain;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Calendar;
import java.util.List;

@Entity
@Table
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "categoryId", nullable = false)
    private Category category;

    @OneToOne
    @JoinColumn(name = "publisherId", nullable = false)
    private Publisher publisher;

    @OneToOne
    @JoinColumn(name = "languageId", nullable = false)
    private Language language;

    @ManyToMany
    @JoinTable(
            name="BookAuthors",
            joinColumns={@JoinColumn(name="bookId", referencedColumnName="id")},
            inverseJoinColumns={@JoinColumn(name="authorId", referencedColumnName="id")})
    private List<Author> authors;

    @Column(name = "year")
    @NotNull
    private Integer year;

    @Column(name = "ISBN", length = 45)
    private String isbn;
    @Column(name = "title", length = 45)
    @NotNull
    private String title;

    @Column(name = "volume", length = 45)
    private String volume;

    @Column(name = "description", length = 45)
    private String description;

    String contentType;
    @Column(name = "picture")
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] picture;

    @Column(name = "dateAdded")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "MM")
    private Calendar dateAdded;

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Calendar getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Calendar dateAdded) {
        this.dateAdded = dateAdded;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public String toString() {
        return title;
    }
}
