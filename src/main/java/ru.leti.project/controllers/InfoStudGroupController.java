package ru.leti.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.leti.project.dao.InfoStudGroupDAO;
import ru.leti.project.models.Group;


import javax.validation.Valid;

@Controller
@RequestMapping(value = "/fkti/groups")
public class InfoStudGroupController {

    private final InfoStudGroupDAO infoStudGroupDAO;

    @Autowired
    public InfoStudGroupController(InfoStudGroupDAO infoStudGroupDAO) {
        this.infoStudGroupDAO = infoStudGroupDAO;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("groups", infoStudGroupDAO.index());
        return "info/enum_of_groups";
    }




    @GetMapping("/new")
    public String newGroup(@ModelAttribute("group") Group group) {
        return "info/new_group";
    }

    @PostMapping()
    public String createGroup(@ModelAttribute("group")  Group group,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "fkti/groups/new";

        infoStudGroupDAO.save(group);
        return "redirect:/fkti/groups";
    }


    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        infoStudGroupDAO.delete(id);
        return "redirect:/fkti/groups";
    }
}