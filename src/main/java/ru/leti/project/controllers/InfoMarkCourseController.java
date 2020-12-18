package ru.leti.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.leti.project.dao.InfoMarkCourseDAO;
import ru.leti.project.models.CourseInfo;
import ru.leti.project.models.Path;

import javax.validation.Valid;
import java.sql.Date;

@Controller
@RequestMapping(value = "/fkti/groups/list/course/mark")
public class InfoMarkCourseController {
    private final InfoMarkCourseDAO infoMarkCourseDAO;

    @Autowired
    public InfoMarkCourseController(InfoMarkCourseDAO infoMarkCourseDAO) {
        this.infoMarkCourseDAO = infoMarkCourseDAO;
    }

    @GetMapping("/{id}/{course}/{year}")
    public String index(Model model, @PathVariable("year") Date year, @PathVariable("course") String course, @PathVariable("id") int id) {
        model.addAttribute("marks", infoMarkCourseDAO.index(id, course, year));

        return "info/view_list_with_mark";
    }


    @GetMapping("/{id}/{course}/{year}/edit/{number}")
    public String edit(Model model, @PathVariable("id") int id, @PathVariable("number") int number, @PathVariable("course") String course, @PathVariable("year") Date year) {
        model.addAttribute("courseInfo", infoMarkCourseDAO.show(id, number, course, year));
        model.addAttribute("course", course);
        model.addAttribute("year", year);
        return "info/edit_mark";
    }

    @PatchMapping("/{id}/{course}/{year}")
    public String update(@ModelAttribute("courseInfo")@Valid CourseInfo courseInfo, BindingResult bindingResult, @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
          return "info/edit_mark";

        infoMarkCourseDAO.update(courseInfo);
        return "redirect:/fkti/groups/list/course/mark/{id}/{course}/{year}";
    }

    @GetMapping("{id}/{course}/{year}/download")
    public String createPathFile(@ModelAttribute("path") Path path, @PathVariable("year") Date year, @PathVariable("course") String course, @PathVariable("id") int id) {

        return "info/create_csv_file";

    }

    @PostMapping("{id}/{course}/{year}/download/csv")
    public String downloadFile(@ModelAttribute("path") Path path, @PathVariable("year") Date year, @PathVariable("course") String course, @PathVariable("id") int id) {
        infoMarkCourseDAO.createCSVFile(path.getPath(), id, course, year);
        return "redirect:/fkti/groups/list/course/mark/{id}/{course}/{year}";
    }

}
