package com.example.spring_data_intro_exercise.services.impl;

import com.example.spring_data_intro_exercise.entities.Author;
import com.example.spring_data_intro_exercise.repositories.AuthorRepository;
import com.example.spring_data_intro_exercise.services.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }


    @Override
    public Author getRandomAuthor() {
        long count = this.authorRepository.count();

        Random random = new Random();
        int authorId = random.nextInt((int)count) + 1;

        return authorRepository.findById(authorId).get();
    }
}
