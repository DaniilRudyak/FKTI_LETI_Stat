package ru.leti.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.leti.project.dao.InfoStudDAO;
import ru.leti.project.models.Student;

import javax.servlet.http.HttpServletRequest;
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
    public String index(HttpServletRequest request, Model model, @PathVariable("id") int id) {
        model.addAttribute("students", infoStudDAO.index(id));
        model.addAttribute("id", id);
        if (request.isUserInRole("ROLE_ADMIN")||request.isUserInRole("ROLE_TEACHER") )
        {
            return "management/view_list_group";
        }
        else
            return "info/view_list_group";
    }

    @GetMapping("/{id}/new")
    public String newStudent(@ModelAttribute("student") Student student, @PathVariable("id") int id, Model model) {
        model.addAttribute("id", id);
        return "management/new_student";
    }

    @PostMapping("/{id}")
    public String createStudent(@ModelAttribute("student") @Valid Student student, BindingResult bindingResult, @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
            return "management/new_student";

        if (infoStudDAO.save(student, id) == 1)
            return "error/error_new_student";
        else
            return "redirect:/fkti/groups/list/students/{id}";
    }


    @DeleteMapping("/{id}/{number}")
    public String delete(@PathVariable("id") int id, @PathVariable("number") int number) {
        infoStudDAO.delete(id, number);
        return "redirect:/fkti/groups/list/students/{id}";
    }

    @GetMapping("/{idGroup}/{number}/{idStudent}")
    public String edit(@PathVariable("idGroup") int idGroup, @PathVariable("number") int number, @PathVariable("idStudent") int idStudent, Model model) {
        model.addAttribute("student", infoStudDAO.show(idStudent, number));

        return "management/edit_student";
    }

    @PatchMapping("/{idGroup}/{number}/{idStudent}")
    public String update(@ModelAttribute("student") @Valid Student student, @PathVariable("idGroup") int idGroup, @PathVariable("idStudent") int idStudent, @PathVariable("number") int number, BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            return "management/edit_student";

        infoStudDAO.update(student);
        return "redirect:/fkti/groups/list/students/{idGroup}";
    }

}