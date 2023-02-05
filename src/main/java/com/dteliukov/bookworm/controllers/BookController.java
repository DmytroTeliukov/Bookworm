package com.dteliukov.bookworm.controllers;

import com.dteliukov.bookworm.models.entities.Book;
import com.dteliukov.bookworm.models.entities.Location;
import com.dteliukov.bookworm.models.entities.Review;
import com.dteliukov.bookworm.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;
    private final ProfileService profileService;
    private final CategoryService categoryService;
    private final AuthorService authorService;

    @Autowired
    public BookController(BookService bookService,
                          ProfileService profileService,
                          CategoryService categoryService,
                          AuthorService authorService) {
        this.bookService = bookService;
        this.profileService = profileService;
        this.categoryService = categoryService;
        this.authorService = authorService;
    }

    @GetMapping
    public String load(Model model) {
        var user = profileService.get();
        model.addAttribute("books", bookService.load(user));
        model.addAttribute("role", user.getRole().name());

        return "books/list";
    }

    @GetMapping("/{id}")
    public String getBook(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", bookService.get(id));
        model.addAttribute("role", profileService.get().getRole().name());

        return "books/profile";
    }

    @GetMapping("/new")
    public String addBook(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("authors", authorService.load());
        model.addAttribute("categories", categoryService.load());

        return "books/new";
    }

    @PostMapping
    public String createBook(@RequestParam("shelf_number") int shelfNumber,
                             @RequestParam("room_number") int roomNumber,
                             @ModelAttribute("book") @Valid Book book,
                             BindingResult bindingResult) {


        Location location = new Location();

        location.setShelfNumber(shelfNumber);
        location.setRoomNumber(roomNumber);
        bookService.save(book, location);

        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable("id") int id) {
        bookService.delete(id);

        return "redirect:/books";
    }

    @GetMapping("/{id}/reviews/new")
    public String addReview(Model model, @PathVariable int id) {
        model.addAttribute("book", bookService.get(id));
        model.addAttribute("review", new Review());

        return "reviews/new";
    }

    @GetMapping("/{id}/reviews")
    public String loadReviews(Model model, @PathVariable int id) {
        var book = bookService.get(id);

        model.addAttribute("role", profileService.get().getRole().name());
        model.addAttribute("book", bookService.get(id));
        model.addAttribute("reviews", book.getReviews());

        return "reviews/list";
    }

    @PostMapping("/{id}/reviews")
    public String createReview(@ModelAttribute("review") Review review,
                               @PathVariable int id) {

        review.setMember(profileService.get().getMember());
        bookService.saveReview(id, review);

        return ("redirect:/books");
    }
}
