package com.dteliukov.bookworm.models.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "location")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "shelf_number")
    private int shelfNumber;

    @Column(name = "room_number")
    private int roomNumber;

    @OneToOne
    @JoinColumn(referencedColumnName = "id", name = "book_id")
    private Book book;
}
