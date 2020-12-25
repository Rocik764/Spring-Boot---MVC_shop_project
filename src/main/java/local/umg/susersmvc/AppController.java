package local.umg.susersmvc;

import local.umg.susersmvc.model.User;
import local.umg.susersmvc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
        return "/shop_pages/index";
    }

    @RequestMapping("/listUsers")
    public String viewListUsers(Model model) {

        List<User> listUsers = service.listAll();
        model.addAttribute("listUsers", listUsers);
        return "/admin_pages/users_list";
    }

    @RequestMapping("/new")
    public String showNewFormUser(Model model) {
        User nuser = new User();
        model.addAttribute("user", nuser);
        return "/admin_pages/new_user";
    }

    // akcja zapisu danych
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String saveUser(@ModelAttribute("user") User user) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        service.save(user);

        return "/login_pages/register_success";
    }

    // akcja zapisu danych
    @RequestMapping(value = "/editUser", method = RequestMethod.POST)
    public String editUser(@ModelAttribute("user") User user, Model model) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        service.save(user);
        List<User> listUsers = service.listAll();
        model.addAttribute("listUsers", listUsers);
        return "/admin_pages/users_list";
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

        return "/login_pages/signup_form";
    }

    @RequestMapping(value = "/show_products", method = RequestMethod.GET)
    public String showProducts(@RequestParam(value = "kategoria", defaultValue = "pies-karma") String category, Model model) {
        //List<User> listUsers = null;

        if(category.equals("pies-karma")) {
            model.addAttribute("kategoria", "Pies-karma");
        }
        else if(category.equals("pies-zabawki")) {
            model.addAttribute("kategoria", "Pies-zabawki");
        }
        else if(category.equals("pies-akcesoria")) {
            model.addAttribute("kategoria", "Pies-akcesoria");
        }
        else if(category.equals("kot-karma")) {
            model.addAttribute("kategoria", "Kot-karma");
        }
        else if(category.equals("kot-zabawki")) {
            model.addAttribute("kategoria", "Kot-zabawki");
        }
        else if(category.equals("kot-akcesoria")) {
            model.addAttribute("kategoria", "Kot-akcesoria");
        }
        else if(category.equals("ryby-karma")) {
            model.addAttribute("kategoria", "Ryby-karma");
        }
        else if(category.equals("ryby-zabawki")) {
            model.addAttribute("kategoria", "Ryby-zabawki");
        }
        else if(category.equals("ryby-akcesoria")) {
            model.addAttribute("kategoria", "Ryby-akcesoria");
        }

        return "/shop_pages/show_products";
    }

    @RequestMapping("/schronisko")
    public String viewSchronisko(Model model) {

        return "/shop_pages/schronisko";
    }

    @RequestMapping("/contact")
    public String viewContact(Model model) {

        return "/shop_pages/contact";
    }

    @RequestMapping("/login")
    public String login() {
        return "/login_pages/login_form";
    }

    @RequestMapping("/logout")
    public String logout() {
        return "/login_pages/login_form";
    }

    @RequestMapping("/profile")
    public String viewUserProfile(@AuthenticationPrincipal CustomUserDetails user, Model model) {
        model.addAttribute("user", user);
        return "/user_pages/user_profile";
    }
}
