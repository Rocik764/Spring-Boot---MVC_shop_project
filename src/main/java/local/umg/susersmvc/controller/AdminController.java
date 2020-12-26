package local.umg.susersmvc.controller;

import local.umg.susersmvc.details.CustomUserDetails;
import local.umg.susersmvc.model.User;
import local.umg.susersmvc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("admin")
public class AdminController {

    @Autowired
    private UserService service;

    @RequestMapping("/listUsers")
    public String viewListUsers(Model model) {

        List<User> listUsers = service.listAll();
        model.addAttribute("listUsers", listUsers);
        return "/admin_pages/users_list";
    }

    // akcja zapisu danych
    @RequestMapping(value = "/editUser", method = RequestMethod.POST)
    public String editUser(@ModelAttribute("user") User user, Model model) {
        service.editUser(user);
        return "redirect:/admin/listUsers";
    }

    // akcja edycji danych dla wskazanego id użytkownika
    @RequestMapping("/edit/{id}")
    public String showEditFormUser(@PathVariable(name = "id") Long id, Model model) {
        //ModelAndView mav = new ModelAndView("admin_pages/edit_user");
        User euser = service.get(id);
        model.addAttribute("user", euser);
        return "/admin_pages/edit_user";
    }

    // akcja usuwania danych dla wskazanego id użytkownika
    @RequestMapping("/delete/{id}")
    public String deleteUser(@PathVariable(name = "id") Long id) {
        service.delete(id);
        return "redirect:/admin/listUsers";
    }

    @RequestMapping(value = "/editMail", method = RequestMethod.POST)
    public String editUserMail(@ModelAttribute("user") User user) {
        Long id = user.getId();
        String email = user.getEmail();
        System.out.println(email + " : " + id);
        service.editMail(email, id);

        return "redirect:/admin/listUsers";
    }

    @RequestMapping(value = "/editFirstName", method = RequestMethod.POST)
    public String editUserFirstName(@ModelAttribute("user") User user) {
        Long id = user.getId();
        String first_name = user.getFirst_name();
        System.out.println(first_name + " : " + id);
        service.editFirstName(first_name, id);

        return "redirect:/admin/listUsers";
    }

    @RequestMapping(value = "/editLastName", method = RequestMethod.POST)
    public String editUserLastName(@ModelAttribute("user") User user) {
        Long id = user.getId();
        String last_name = user.getLast_name();
        System.out.println(user.getLast_name() + " : " + id);
        service.editLastName(last_name, id);

        return "redirect:/admin/listUsers";
    }

    @RequestMapping(value = "/editPassword", method = RequestMethod.POST)
    public String editUserPassword(@ModelAttribute("user") User user) {
        Long id = user.getId();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        String password = user.getPassword();
        System.out.println(user.getPassword() + " : " + id);
        service.editPassword(password, id);

        return "redirect:/admin/listUsers";
    }

    // akcja zapisu danych
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String saveUser(@ModelAttribute("user") User user) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        service.save(user);

        return "redirect:/admin/listUsers";
    }
}
