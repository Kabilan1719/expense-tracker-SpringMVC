package com.example.demo.controller;

import com.example.demo.dto.ExpenseDTO;
import com.example.demo.entity.ExpenseEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.ExpenseRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.ExpenseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Controller
public class ExpenseController {

    @Autowired
    private ExpenseService service;

    @Autowired
    private ExpenseRepository expenseRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder encoder;

    @GetMapping("/")
    public String home(Model model, Principal principal) {

        List<ExpenseEntity> list = expenseRepo.findAll();
        Double total = service.getTotalAmount(list);

        model.addAttribute("expenses", list);
        model.addAttribute("totalAmount", total);
        model.addAttribute("periodAmount", 0);
        model.addAttribute("categoryAmount", 0);
        
        if (principal != null) {
            model.addAttribute("username", principal.getName());
        }

        return "index";
    }

    @GetMapping("/filter")
    public String filter(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Integer days,
            Model model) {

        List<ExpenseEntity> list;
        Double periodTotal = 0.0;
        Double categoryTotal = 0.0;

        if (category != null && !category.equals("All")) {

            list = expenseRepo.findByCategory(category);
            categoryTotal = service.getTotalAmount(list);

            model.addAttribute("category", category);
        }
        else if (days != null) {

            if (days == 0) {
                list = expenseRepo.findByDate(LocalDate.now());
                model.addAttribute("period", "Today");
            } else {
                list = expenseRepo.findByDateAfter(LocalDate.now().minusDays(days));
                model.addAttribute("period", "Last " + days + " days");
            }

            periodTotal = service.getTotalAmount(list);
        }
        else {
            list = expenseRepo.findAll();
        }

        Double total = service.getTotalAmount(expenseRepo.findAll());

        model.addAttribute("expenses", list);
        model.addAttribute("totalAmount", total);
        model.addAttribute("periodAmount", periodTotal);
        model.addAttribute("categoryAmount", categoryTotal);

        return "index";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        expenseRepo.deleteById(id);
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable Long id, Model model) {

        ExpenseEntity exp = expenseRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        model.addAttribute("expense", exp);

        return "edit";
    }

    @PostMapping("/update")
    public String update(ExpenseEntity exp) {
        expenseRepo.save(exp);
        return "redirect:/";
    }
    
    @PostMapping("/add")
    public String addExpense(ExpenseDTO dto) {
    	service.saveExpense(dto);
    	return "redirect:/";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password) {

        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(encoder.encode(password));
        user.setRole("USER");

        userRepo.save(user);

        return "redirect:/login";
    }
}