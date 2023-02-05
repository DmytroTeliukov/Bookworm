package com.dteliukov.bookworm.repositories;

import com.dteliukov.bookworm.models.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    @Query("SELECT b FROM Book b where b.id not in " +
            "(select r.book.id from Reservation r, Member m, User u " +
            "where r.member.id = m.id and m.user.id = u.id and r.reservationStatus = 'RETURNED' and u.email != (?1))")
    List<Book> findAllNotByEmail(String email);


}
