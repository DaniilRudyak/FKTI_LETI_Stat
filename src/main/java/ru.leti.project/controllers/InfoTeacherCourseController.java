package ru.leti.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.leti.project.dao.InfoTeacherCourseDAO;
import ru.leti.project.models.Teacher;


@Controller
@RequestMapping(value = "/fkti/groups/list/course")
public class InfoTeacherCourseController {
    private final InfoTeacherCourseDAO infoTeacherCourseDAO;

    @Autowired
    public InfoTeacherCourseController(InfoTeacherCourseDAO infoTeacherCourseDAO) {
        this.infoTeacherCourseDAO = infoTeacherCourseDAO;
    }

    @GetMapping("/{id}")
    public String index(Model model, @PathVariable("id") int id) {
        model.addAttribute("teachers", infoTeacherCourseDAO.index(id));
        model.addAttribute("id", id);
        return "info/enum_of_course";
    }

    // @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {//
       // model.addAttribute("person", infoTeacheCourseDAO.show(id));
        return "people/show";
    }


    @GetMapping("/{id}/new")
    public String newTeacher(@ModelAttribute("teacher") Teacher teacher, @PathVariable("id") int id, Model model) {
        model.addAttribute("id", id);
        return "info/new_course";
    }

    @PostMapping("/{id}")
    public String createTeacher(@ModelAttribute("teacher") Teacher teacher, BindingResult bindingResult, @PathVariable("id") int id) {
        //if (bindingResult.hasErrors())
        //  return "fkti/groups/new";

        infoTeacherCourseDAO.save(teacher,id);
        return "redirect:/fkti/groups/list/course/{id}";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        infoTeacherCourseDAO.delete(id);
        return "redirect:/fkti/groups/list/course/{id}";
    }
}
