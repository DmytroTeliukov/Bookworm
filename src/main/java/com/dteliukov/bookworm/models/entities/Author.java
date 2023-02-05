package com.dteliukov.bookworm.models.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "author")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotEmpty(message = "Lastname should not be empty")
    @Size(min = 2, max = 30, message = "Size should be between 2 and 30")
    @Column(name = "lastname")
    private String lastname;

    @NotEmpty(message = "Firstname should not be empty")
    @Size(min = 2, max = 30, message = "Size should be between 2 and 30")
    @Column(name = "firstname")
    private String firstname;


    @Min(value = 1500, message = "Year of birth should be bigger than 1500")
    @Column(name = "year_birth")
    private int yearBirth;

    @Column(name = "year_death")
    private int yearDeath;

    @NotEmpty(message = "Biography should not be empty")
    @Column(name = "biography")
    private String biography;

    @OneToMany(mappedBy = "author")
    private Set<Book> books;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return id == author.id &&
                Objects.equals(lastname, author.lastname) &&
                Objects.equals(firstname, author.firstname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lastname, firstname);
    }
}
