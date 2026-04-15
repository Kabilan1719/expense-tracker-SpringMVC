package com.example.demo.repository;
import com.example.demo.entity.ExpenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Long> {

    // Filter by category
    List<ExpenseEntity> findByCategory(String category);

    // Filter by date (last days)
    List<ExpenseEntity> findByDateAfter(LocalDate date);
    
    List<ExpenseEntity> findByDate(LocalDate date);
}