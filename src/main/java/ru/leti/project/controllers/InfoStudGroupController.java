package ru.leti.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.leti.project.dao.InfoStudGroupDAO;
import ru.leti.project.models.Group;

import javax.servlet.http.HttpServletRequest;
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
    public String index(HttpServletRequest request,Model model) {
        model.addAttribute("groups", infoStudGroupDAO.index());
        if (request.isUserInRole("ROLE_ADMIN")||request.isUserInRole("ROLE_TEACHER") )
        {
            return "management/enum_of_groups";
        }
        else
            return"info/enum_of_groups";

    }


    @GetMapping("/new")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public String newGroup( @ModelAttribute("group") Group group) {
        return "management/new_group";
    }

    @PostMapping()
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public String createGroup(@ModelAttribute("group") @Valid Group group,
                              BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "management/new_group";

        if (infoStudGroupDAO.save(group) == 1)
            return "error/error_new_group";
        else
            return "redirect:/fkti/groups";
    }

    @GetMapping("{idGroup}/edit")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public String edit(@PathVariable("idGroup") int idGroup, Model model){
        model.addAttribute("group", infoStudGroupDAO.show(idGroup));
        return "management/edit_group";

    }
    @PatchMapping("{idGroup}/edit")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public String update(@ModelAttribute("group") Group group,
                         @PathVariable("idGroup") int idGroup,
                         BindingResult bindingResult){
    if (bindingResult.hasErrors())
        return "management/edit_group";

    infoStudGroupDAO.update(group);
        return "redirect:/fkti/groups";

}

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public String delete(@PathVariable("id") int id) {
        infoStudGroupDAO.delete(id);
        return "redirect:/fkti/groups";
    }
}