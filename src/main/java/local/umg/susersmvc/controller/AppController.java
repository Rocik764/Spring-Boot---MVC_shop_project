package local.umg.susersmvc.controller;

import local.umg.susersmvc.details.CustomUserDetails;
import local.umg.susersmvc.model.Producent;
import local.umg.susersmvc.model.User;
import local.umg.susersmvc.service.ProducentService;
import local.umg.susersmvc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("app")
public class AppController {

    @Autowired
    private UserService service;

    @Autowired
    private ProducentService producentService;

    //  akcje kontrolera aplikacji
    @GetMapping("")
    public String viewHomePage() {
        return "/shop_pages/index";
    }

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

    @RequestMapping("/partners")
    public String viewPartners(Model model) {
        List<Producent> listProducent = producentService.listAll();
        model.addAttribute("listProducent", listProducent);
        return "/shop_pages/partners";
    }

    @RequestMapping("/login")
    public String login() {
        return "/login_pages/login_form";
    }

    @RequestMapping("/profile")
    public String viewUserProfile(@AuthenticationPrincipal CustomUserDetails user, Model model) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        model.addAttribute("user", user);
        return "/user_pages/user_profile";
    }
}
