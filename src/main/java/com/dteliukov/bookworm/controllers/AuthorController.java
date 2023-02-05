package com.dteliukov.bookworm.controllers;

import com.dteliukov.bookworm.models.entities.Author;
import com.dteliukov.bookworm.services.AuthorService;
import com.dteliukov.bookworm.services.ProfileService;
import com.dteliukov.bookworm.utils.AuthorYearsValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService authorService;
    private final ProfileService profileService;
    private final AuthorYearsValidator authorYearsValidator;

    @Autowired
    public AuthorController(AuthorService authorService,
                            ProfileService profileService,
                            AuthorYearsValidator authorYearsValidator) {
        this.authorService = authorService;
        this.profileService = profileService;
        this.authorYearsValidator = authorYearsValidator;
    }

    @GetMapping
    public String loadAuthors(Model model) {
        model.addAttribute("authors", authorService.load());
        model.addAttribute("role", profileService.get().getRole().name());
        return "authors/list";
    }

    @GetMapping("/{id}")
    public String getAuthor(Model model, @PathVariable("id") int id) {
        model.addAttribute("author", authorService.get(id));
        model.addAttribute("role", profileService.get().getRole().name());
        return "authors/profile";
    }

    @GetMapping("/new")
    public String addAuthor(Model model) {
        model.addAttribute("author", new Author());
        return "authors/new";
    }

    @PostMapping
    public String createAuthor(@ModelAttribute("author") @Valid Author author,
                               BindingResult bindingResult) {
        authorYearsValidator.validate(author, bindingResult);
        if (bindingResult.hasErrors())
            return "authors/new";

        authorService.save(author);
        return "redirect:/authors";
    }

    @GetMapping("/{id}/edit")
    public String editAuthor(@PathVariable("id") int id, Model model) {
        model.addAttribute("author", authorService.get(id));
        return "authors/edit";
    }

    @PatchMapping("/{id}")
    public String updateAuthor(@PathVariable("id") int id,
                               @ModelAttribute("author") @Valid Author author,
                               BindingResult bindingResult) {
        authorYearsValidator.validate(author, bindingResult);
        if (bindingResult.hasErrors())
            return "authors/edit";

        author.setId(id);
        authorService.save(author);
        return "redirect:/authors";
    }

    @DeleteMapping("/{id}")
    public String deleteAuthor(@PathVariable("id") int id) {
        authorService.delete(id);
        return "redirect:/authors";
    }
}
