package com.spring.seoulmoaapi.domain.event.repository;

import com.spring.seoulmoaapi.domain.event.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}