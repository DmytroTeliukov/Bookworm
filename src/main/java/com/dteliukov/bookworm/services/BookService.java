package com.dteliukov.bookworm.services;

import com.dteliukov.bookworm.models.entities.Book;
import com.dteliukov.bookworm.models.entities.Location;
import com.dteliukov.bookworm.models.entities.Review;
import com.dteliukov.bookworm.models.entities.User;
import com.dteliukov.bookworm.models.enums.BookStatus;
import com.dteliukov.bookworm.models.enums.Role;
import com.dteliukov.bookworm.repositories.BookLocationRepository;
import com.dteliukov.bookworm.repositories.BookRepository;
import com.dteliukov.bookworm.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

@Service
public class BookService {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");
    private final BookRepository bookRepository;
    private final BookLocationRepository bookLocationRepository;
    private final ReviewRepository reviewRepository;

    @Autowired
    public BookService(BookRepository bookRepository,
                       BookLocationRepository bookLocationRepository,
                       ReviewRepository reviewRepository) {
        this.bookRepository = bookRepository;
        this.bookLocationRepository = bookLocationRepository;
        this.reviewRepository = reviewRepository;
    }

    public List<Book> load(User user) {
        if (user.getRole().equals(Role.MEMBER))
            return bookRepository.findAllNotByEmail(user.getEmail());
        else
            return bookRepository.findAll();
    }

    public void save(Book book, Location location) {
        book.setAvgRate(0.0f);
        book.setCountComments(0);
        book.setBookStatus(BookStatus.ACCESSIBLE);
        var savedBook = bookRepository.save(book);
        location.setBook(savedBook);
        bookLocationRepository.save(location);
    }

    public Book get(int id) {
        return bookRepository.findById(id).get();
    }

    public void delete(int id) {
        var book = get(id);

        bookLocationRepository.delete(book.getLocation());
        bookRepository.delete(book);
    }

    public void saveReview(int bookId, Review review) {
        var book = bookRepository
                .findById(bookId)
                .get();

        review.setPosted(new Date());
        review.setBook(book);

        book.setCountComments(book.getCountComments() + 1);
        book.getReviews().add(reviewRepository.save(review));

        updateBookReviewsRate(book);
    }

    private void updateBookReviewsRate(Book book) {
        var avgRate = book.getReviews().stream()
                .mapToDouble(rate -> Double.parseDouble(String.valueOf(rate.getRate())))
                .average()
                .getAsDouble();

        book.setAvgRate(Float.parseFloat(DECIMAL_FORMAT.format(avgRate).replace(",", ".")));
        bookRepository.save(book);
    }
}
