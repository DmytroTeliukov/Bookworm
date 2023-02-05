package com.dteliukov.bookworm.services;

import com.dteliukov.bookworm.models.entities.Category;
import com.dteliukov.bookworm.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> load() {
        return categoryRepository.findAll();
    }

    public void save(Category category) {
        categoryRepository.save(category);
    }

    public void delete(int id) {
        categoryRepository.delete(categoryRepository.findById(id).get());
    }
}
