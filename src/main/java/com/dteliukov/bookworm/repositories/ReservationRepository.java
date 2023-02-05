package com.dteliukov.bookworm.repositories;

import com.dteliukov.bookworm.models.entities.Book;
import com.dteliukov.bookworm.models.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    @Query("select r from Reservation r, Member m, User u " +
            "where r.member.id = m.id and m.user.id = u.id and u.email = (?1)")
    List<Reservation> findAllByEmail(String email);


}
