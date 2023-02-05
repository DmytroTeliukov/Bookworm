package com.dteliukov.bookworm.models.entities;

import com.dteliukov.bookworm.models.enums.BookStatus;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotEmpty(message = "Title should not be empty")
    @Column(name = "title")
    private String title;

    @Min(value = 1000, message = "Year of publication should be greater 1000")
    @Max(value = 2200, message = "Year of publication should be less 2200")
    @Column(name = "publication_year")
    private int publicationYear;

    @NotEmpty(message = "Publisher should not be empty")
    @Column(name = "publisher")
    private String publisher;

    @Column(name = "pages")
    private int pages;

    @Column(name = "avg_rate")
    private float avgRate;

    @Column(name = "count_comments")
    private int countComments;

   @Column(name = "count_in_shelf")
    private int countInShelf;

    @Column(name = "count_in_stock")
    private int countInStock;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BookStatus bookStatus;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id", name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id", name = "author_id")
    private Author author;

    @OneToOne(mappedBy = "book")
    private Location location;

    @OneToMany(mappedBy = "book")
    private Set<Reservation> reservations;

    @OneToMany(mappedBy = "book")
    private Set<Review> reviews;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return id == book.id &&
                publicationYear == book.publicationYear &&
                pages == book.pages &&
                Float.compare(book.avgRate, avgRate) == 0 &&
                countComments == book.countComments &&
                countInShelf == book.countInShelf &&
                countInStock == book.countInStock &&
                Objects.equals(title, book.title) &&
                Objects.equals(publisher, book.publisher) &&
                bookStatus == book.bookStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, publicationYear, publisher, pages, avgRate, countComments, countInShelf, countInStock, bookStatus);
    }
}
