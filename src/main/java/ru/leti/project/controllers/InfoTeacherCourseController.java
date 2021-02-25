package ru.leti.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.leti.project.dao.InfoTeacherCourseDAO;
import ru.leti.project.models.Teacher;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.sql.Date;


@Controller
@RequestMapping(value = "/fkti/groups/list/course")
public class InfoTeacherCourseController {
    private final InfoTeacherCourseDAO infoTeacherCourseDAO;

    @Autowired
    public InfoTeacherCourseController(InfoTeacherCourseDAO infoTeacherCourseDAO) {
        this.infoTeacherCourseDAO = infoTeacherCourseDAO;
    }

    @GetMapping("/{id}")
    public String index(HttpServletRequest request, Model model, @PathVariable("id") int id) {
        model.addAttribute("teachers", infoTeacherCourseDAO.index(id));
        model.addAttribute("id", id);
        if (request.isUserInRole("ROLE_ADMIN")||request.isUserInRole("ROLE_TEACHER") )
        {
            return "management/enum_of_course";
        }
        else
            return "info/enum_of_course";

    }

    @GetMapping("/{id}/new")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public String newTeacher(@ModelAttribute("teacher") Teacher teacher, @PathVariable("id") int id, Model model) {
        model.addAttribute("id", id);
        return "management/new_course";
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public String createTeacher(@ModelAttribute("teacher") @Valid Teacher teacher, BindingResult bindingResult, @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
            return "management/new_course";

        if (infoTeacherCourseDAO.save(teacher, id) == 1)
            return "error/error_new_course";
        else
            return "redirect:/fkti/groups/list/course/{id}";
    }

    @DeleteMapping("/{id}/{course}/{year}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public String delete(@PathVariable("id") int id, @PathVariable("course") String course, @PathVariable("year") Date year) {
        infoTeacherCourseDAO.delete(id, course, year);
        return "redirect:/fkti/groups/list/course/{id}";
    }

    @GetMapping("/{idGroup}/{course}/{year}/{idTeacher}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public String edit(@PathVariable("idGroup") int idGroup, @PathVariable("course") String course, @PathVariable("year") Date year, @PathVariable("idTeacher") int idTeacher, Model model) {
        model.addAttribute("teacher", infoTeacherCourseDAO.show(idTeacher));
        return "management/edit_course";
    }

    @PatchMapping("/{idGroup}/{course}/{year}/{idTeacher}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public String update(@ModelAttribute("teacher") @Valid Teacher teacher, @PathVariable("idGroup") int idGroup, @PathVariable("course") String course, @PathVariable("year") Date year, @PathVariable("idTeacher") int idTeacher, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "management/edit_course";

        infoTeacherCourseDAO.update(teacher);
        return "redirect:/fkti/groups/list/course/{idGroup}";
    }

}
