package ru.leti.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.leti.project.auth.ApplicationUser;
import ru.leti.project.auth.ApplicationUserService;
import ru.leti.project.auth.ApplicationUserDaoImpl;

import javax.validation.Valid;

@Controller
@RequestMapping("/")
public class TemplateController {

    @GetMapping("login")
    public String getLogin(){
        return "login";
    }
    @Autowired
    private ApplicationUserService applicationUserService;

    public TemplateController(ApplicationUserDaoImpl applicationUserDaoImpl) {
        this.applicationUserDaoImpl = applicationUserDaoImpl;
    }

    private final ApplicationUserDaoImpl applicationUserDaoImpl;

    @GetMapping("registration")
    public String registration(Model model) {
        model.addAttribute("userForm", new ApplicationUser());
        return "registration";
    }

    @PostMapping("registration")
    public String addUser(@ModelAttribute("userForm") @Valid ApplicationUser userForm, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "registration";
        }
        if ( applicationUserDaoImpl.save(userForm)==0 ){
            model.addAttribute("usernameError", "Пользователь с таким именем уже существует");
            return "error/error_registration";
        }
        return "login";
    }
}
