package com.spring.seoulmoaapi.domain.event.service;

import com.spring.seoulmoaapi.domain.event.entity.Category;
import com.spring.seoulmoaapi.domain.event.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;


    public List<Category> findAll(){
        return categoryRepository.findAll();
    }
}
