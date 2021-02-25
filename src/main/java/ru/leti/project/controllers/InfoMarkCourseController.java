package ru.leti.project.controllers;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.leti.project.dao.InfoMarkCourseDAO;
import ru.leti.project.models.CourseInfo;
import ru.leti.project.models.Path;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.sql.Date;

@Controller
@RequestMapping(value = "/fkti/groups/list/course/mark")
@AllArgsConstructor
public class InfoMarkCourseController {
    private final InfoMarkCourseDAO infoMarkCourseDAO;

    @GetMapping("/{id}/{course}/{year}")
    public String index(HttpServletRequest request, Model model, @PathVariable("year") Date year, @PathVariable("course") String course, @PathVariable("id") int id) {
        model.addAttribute("marks", infoMarkCourseDAO.index(id, course, year));
        if (request.isUserInRole("ROLE_ADMIN")||request.isUserInRole("ROLE_TEACHER") )
        {
            return "management/view_list_with_mark";
        }
        else
            return "info/view_list_with_mark";
    }


    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @GetMapping("/{id}/{course}/{year}/edit/{number}")
    public String edit(Model model, @PathVariable("id") int id, @PathVariable("number") int number, @PathVariable("course") String course, @PathVariable("year") Date year) {
        model.addAttribute("courseInfo", infoMarkCourseDAO.show(id, number, course, year));
        model.addAttribute("course", course);
        model.addAttribute("year", year);
        return "management/edit_mark";
    }

    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @PatchMapping("/{id}/{course}/{year}")
    public String update(@ModelAttribute("courseInfo")@Valid CourseInfo courseInfo, BindingResult bindingResult, @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
          return "management/edit_mark";

        infoMarkCourseDAO.update(courseInfo);
        return "redirect:/fkti/groups/list/course/mark/{id}/{course}/{year}";
    }

    @GetMapping("{id}/{course}/{year}/download")
    public String createPathFile(HttpServletRequest request, @ModelAttribute("path") Path path, @PathVariable("year") Date year, @PathVariable("course") String course, @PathVariable("id") int id) {
        if (request.isUserInRole("ROLE_ADMIN")||request.isUserInRole("ROLE_TEACHER") )
        {
            return "management/create_csv_file";
        }
        else
            return "info/create_csv_file";


    }

    @PostMapping("{id}/{course}/{year}/download/csv")
    public String downloadFile(@ModelAttribute("path") Path path, @PathVariable("year") Date year, @PathVariable("course") String course, @PathVariable("id") int id) {
        infoMarkCourseDAO.createCSVFile(path.getPath(), id, course, year);
        return "redirect:/fkti/groups/list/course/mark/{id}/{course}/{year}";
    }

}
