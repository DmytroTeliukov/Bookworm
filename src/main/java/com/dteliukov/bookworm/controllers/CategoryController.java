package com.dteliukov.bookworm.controllers;

import com.dteliukov.bookworm.models.entities.Category;
import com.dteliukov.bookworm.services.CategoryService;
import com.dteliukov.bookworm.services.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final ProfileService profileService;

    @Autowired
    public CategoryController(CategoryService categoryService,
                              ProfileService profileService) {
        this.categoryService = categoryService;
        this.profileService = profileService;
    }


    @GetMapping
    public String load(Model model) {
        model.addAttribute("categories", categoryService.load());
        model.addAttribute("role", profileService.get().getRole().name());
        return "categories/list";
    }

    @GetMapping("/new")
    public String addCategory(Model model) {
        model.addAttribute("category", new Category());
        return "categories/new";
    }

    @PostMapping
    public String createCategory(@ModelAttribute("category") Category category) {
        categoryService.save(category);

        return "redirect:/categories";
    }

    @DeleteMapping("/{id}")
    public String deleteCategory(@PathVariable("id") int id) {
        categoryService.delete(id);
        return "redirect:/categories";
    }
}
