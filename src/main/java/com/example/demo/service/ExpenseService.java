package com.example.demo.service;

import com.example.demo.dto.ExpenseDTO;
import com.example.demo.entity.ExpenseEntity;
import com.example.demo.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository repo;

    public ExpenseEntity saveExpense(ExpenseDTO dto) {

        ExpenseEntity e = new ExpenseEntity();

        e.setDate(dto.getDate());
        e.setAmount(dto.getAmount());
        e.setCategory(dto.getCategory());
        e.setType(dto.getType());
        e.setReference(dto.getReference());

        return repo.save(e);
    }

    public List<ExpenseEntity> getAllExpenses() {
        return repo.findAll(Sort.by(Sort.Direction.DESC, "date"));
    }

    public List<ExpenseEntity> filterByCategory(String category) {
        return repo.findByCategory(category);
    }

    public List<ExpenseEntity> filterByLastDays(int days) {
        if (days == 0) {
            return repo.findByDate(LocalDate.now());
        }
        return repo.findByDateAfter(LocalDate.now().minusDays(days));
    }

    public double calculateTotal(List<ExpenseEntity> expenses) {
        return expenses.stream()
                .mapToDouble(ExpenseEntity::getAmount)
                .sum();
    }
    
    public Double getTotalAmount(List<ExpenseEntity> list) {
        return list.stream()
                .mapToDouble(ExpenseEntity::getAmount)
                .sum();
    }
}