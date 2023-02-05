package com.dteliukov.bookworm.services;

import com.dteliukov.bookworm.models.entities.Book;
import com.dteliukov.bookworm.models.entities.Member;
import com.dteliukov.bookworm.models.entities.Reservation;
import com.dteliukov.bookworm.models.entities.User;
import com.dteliukov.bookworm.models.enums.BookStatus;
import com.dteliukov.bookworm.models.enums.ReservationStatus;
import com.dteliukov.bookworm.models.enums.Role;
import com.dteliukov.bookworm.repositories.BookRepository;
import com.dteliukov.bookworm.repositories.MemberRepository;
import com.dteliukov.bookworm.repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReservationService {
    public static final int DAYS_BORROWING = 20;

    private final ReservationRepository reservationRepository;
    private final BookRepository bookRepository;

    private final MemberRepository memberRepository;
    @Autowired
    public ReservationService(ReservationRepository reservationRepository,
                              BookRepository bookRepository,
                              MemberRepository memberRepository) {
        this.reservationRepository = reservationRepository;
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
    }



    public List<Reservation> load(User user) {
        List<Reservation> reservations = getReservations(user);

        Objects.requireNonNull(reservations)
                .sort(Comparator.comparing(Reservation::getDeadline));

        return reservations;
    }

    public Reservation get(int id) {
        return reservationRepository.findById(id).get();
    }

    public void save(Reservation reservation) {
        Calendar calendar = Calendar.getInstance();
        Date reserved = calendar.getTime();

        calendar.add(Calendar.DATE, DAYS_BORROWING);
        Date deadline = calendar.getTime();

        changeMemberDataByTakingBook(reservation.getMember());
        takeBook(reservation.getBook());

        reservation.setReserved(reserved);
        reservation.setDeadline(deadline);
        reservation.setReservationStatus(ReservationStatus.RESERVED);

        reservationRepository.save(reservation);
    }

    public void submit(int id) {
        var reservation = get(id);
        var member = reservation.getMember();
        var book = reservation.getBook();

        changeMemberDataByReturningBook(member, reservation.getReservationStatus());
        returnBook(book);

        reservation.setReservationStatus(ReservationStatus.RETURNED);
        reservationRepository.save(reservation);
    }

    private void changeMemberDataByTakingBook(Member member) {
        member.setCountBorrowed(member.getCountBorrowed() + 1);
        memberRepository.save(member);
    }
    private void changeMemberDataByReturningBook(Member member, ReservationStatus status) {
        if (status.equals(ReservationStatus.OUTDATED))
            member.setCountPaidFines(member.getCountPaidFines() + 1);

        member.setCountReturned(member.getCountReturned() + 1);
        memberRepository.save(member);
    }

    private void takeBook(Book book) {
        book.setCountInShelf(book.getCountInShelf() - 1);
        if (book.getCountInStock() == 0 && book.getCountInShelf() == 0)
            book.setBookStatus(BookStatus.EMPTY);

        bookRepository.save(book);
    }

    private void returnBook(Book book) {
        if (book.getBookStatus().equals(BookStatus.EMPTY))
            book.setBookStatus(BookStatus.ACCESSIBLE);

        book.setCountInShelf(book.getCountInShelf() + 1);
        bookRepository.save(book);
    }

    private List<Reservation> getReservations(User user) {
        if (user.getRole().equals(Role.MEMBER))
            return reservationRepository.findAllByEmail(user.getEmail());
        else
            return reservationRepository.findAll();
    }
}
