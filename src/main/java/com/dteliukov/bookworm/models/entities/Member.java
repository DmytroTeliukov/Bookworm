package com.dteliukov.bookworm.models.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Set;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "member")
public class Member {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "count_borrowed")
    private int countBorrowed;

    @Column(name = "count_returned")
    private int countReturned;

    @Column(name = "count_outdated")
    private int countOutdated;

    @Column(name = "count_paid_fines")
    private int countPaidFines;

    @OneToMany(mappedBy = "member")
    private Set<Reservation> reservations;

    @OneToMany(mappedBy = "member")
    private Set<Review> reviews;

    public Member(User user,
                  int countBorrowed,
                  int countReturned,
                  int countOutdated,
                  int countPaidFines) {
        this.user = user;
        this.countBorrowed = countBorrowed;
        this.countReturned = countReturned;
        this.countOutdated = countOutdated;
        this.countPaidFines = countPaidFines;
    }
}
