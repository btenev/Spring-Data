package com.example.spring_data_intro_exercise.services;

import com.example.spring_data_intro_exercise.entities.Category;

import java.util.Set;

public interface CategoryService {
    Set<Category> getRandomCategories();
}
