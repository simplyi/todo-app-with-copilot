package com.appsdeveloperblog.todo.demo.controller;

import com.appsdeveloperblog.todo.demo.dto.UserRegistrationDto;
import com.appsdeveloperblog.todo.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/registration")
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showRegistrationForm(final Model model) {
        model.addAttribute("user", new UserRegistrationDto(null, null, null, null, null));
        return "registration";
    }

    @PostMapping
    public String registerUser(
            @Valid @ModelAttribute("user") final UserRegistrationDto dto,
            final BindingResult result) {

        if (result.hasErrors()) {
            return "registration";
        }

        userService.register(dto);
        return "redirect:/registration?success";
    }
}
