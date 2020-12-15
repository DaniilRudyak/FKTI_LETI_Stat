package ru.leti.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.leti.project.dao.InfoStudDAO;
import ru.leti.project.models.Student;


import javax.validation.Valid;

@Controller
@RequestMapping(value = "/fkti/groups/list/students")
public class InfoStudController {

    private final InfoStudDAO infoStudDAO;

    @Autowired
    public InfoStudController(InfoStudDAO infoStudDAO) {
        this.infoStudDAO = infoStudDAO;
    }

    @GetMapping("/{id}")
    public String index(Model model, @PathVariable("id") int id) {
        model.addAttribute("students", infoStudDAO.index(id));
        model.addAttribute("id", id);
        return "info/view_list_group";
    }


    @GetMapping("/{id}/new")
    public String newStudent(@ModelAttribute("student") Student student, @PathVariable("id") int id, Model model) {
        model.addAttribute("id", id);
        return "info/new_student";
    }

    @PostMapping("/{id}")
    public String createStudent(@ModelAttribute("student") Student student, BindingResult bindingResult, @PathVariable("id") int id) {
        //if (bindingResult.hasErrors())
          //  return "fkti/groups/new";

        infoStudDAO.save(student,id);
        return "redirect:/fkti/groups/list/students/{id}";
    }




    @DeleteMapping("/{id}/{number}")
    public String delete(@PathVariable("id") int id, @PathVariable("number") int number) {
        infoStudDAO.delete(id, number);
        return "redirect:/fkti/groups/list/students/{id}";
    }
}