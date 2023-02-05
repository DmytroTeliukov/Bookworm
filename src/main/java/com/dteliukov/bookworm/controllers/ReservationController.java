package com.dteliukov.bookworm.controllers;

import com.dteliukov.bookworm.models.entities.Reservation;
import com.dteliukov.bookworm.services.BookService;
import com.dteliukov.bookworm.services.ProfileService;
import com.dteliukov.bookworm.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reservations")
public class ReservationController {
    private final ProfileService profileService;
    private final ReservationService reservationService;
    private final BookService bookService;

    @Autowired
    public ReservationController(ProfileService profileService,
                                 ReservationService reservationService,
                                 BookService bookService) {
        this.profileService = profileService;
        this.reservationService = reservationService;
        this.bookService = bookService;
    }

    @GetMapping
    public String load(Model model) {
        var user = profileService.get();

        model.addAttribute("role", user.getRole().name());
        model.addAttribute("reservations", reservationService.load(user));

        return "reservations/list";
    }

    @PostMapping("/{id}")
    public String save(@PathVariable("id") int id) {
        var book = bookService.get(id);
        var user = profileService.get();
        Reservation reservation = new Reservation();

        reservation.setBook(book);
        reservation.setMember(user.getMember());
        reservationService.save(reservation);

        return "redirect:/reservations";
    }

    @PostMapping("/{id}/submit")
    public String submit(@PathVariable("id") int id) {
        reservationService.submit(id);

        return "redirect:/reservations";
    }

}
