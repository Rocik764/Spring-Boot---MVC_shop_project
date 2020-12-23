package local.umg.susersmvc;

import local.umg.susersmvc.model.User;
import local.umg.susersmvc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@Controller
public class AppController {

    @Autowired
    private UserService service;
    //  akcje kontrolera aplikacji

    @GetMapping("")
    public String viewHomePage() {
        return "index";
    }

    @RequestMapping("/listUsers")
    public String viewListUsers(Model model) {

        List<User> listUsers = service.listAll();
        model.addAttribute("listUsers", listUsers);
        return "users_list";
    }

    @RequestMapping("/new")
    public String showNewFormUser(Model model) {
        User nuser = new User();
        model.addAttribute("user", nuser);
        return "new_user";
    }

    // akcja zapisu danych
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String saveUser(@ModelAttribute("user") User user) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        service.save(user);

        return "register_success";
    }

    // akcja edycji danych dla wskazanego id użytkownika
    @RequestMapping("/edit/{id}")
    public ModelAndView showEditFormUser(@PathVariable(name = "id") Long id) {
        ModelAndView mav = new ModelAndView("edit_user");
        User euser = service.get(id);
        mav.addObject("user", euser);
        return mav;
    }

    // akcja usuwania danych dla wskazanego id użytkownika
    @RequestMapping("/delete/{id}")
    public String deleteUser(@PathVariable(name = "id") Long id) {
        service.delete(id);
        return "redirect:/listUsers";
    }

    @RequestMapping(value = "/wlasne", method = RequestMethod.GET)
    public String wlasneList(@RequestParam(value = "choice", defaultValue = "1") int choice, Model model) {
        List<User> listUsers = null;

        if(choice == 0) {
            listUsers = service.listAll();
            model.addAttribute("sort", "nie");
        }
        else if(choice == 1) {
            listUsers = service.listAllAsc();
            model.addAttribute("sort", "Id malejąco");
        }

        model.addAttribute("listUsers", listUsers);
        return "/wlasne";
    }

    /**
     * ---------------------------------------------------------------
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());

        return "signup_form";
    }
}
