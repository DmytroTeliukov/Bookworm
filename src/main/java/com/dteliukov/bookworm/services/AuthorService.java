package com.dteliukov.bookworm.services;

import com.dteliukov.bookworm.models.entities.Author;
import com.dteliukov.bookworm.repositories.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public void save(Author author) {
        authorRepository.save(author);
    }

    public List<Author> load() {
        return authorRepository.findAll();
    }

    public Author get(int id) {
        return authorRepository.findById(id).get();
    }

    public void delete(int id) {
        authorRepository.delete(get(id));
    }
}
