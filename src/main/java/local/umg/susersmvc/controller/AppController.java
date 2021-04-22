package local.umg.susersmvc.controller;

import local.umg.susersmvc.details.CustomUserDetails;
import local.umg.susersmvc.model.Orders;
import local.umg.susersmvc.model.Producent;
import local.umg.susersmvc.model.User;
import local.umg.susersmvc.service.OrdersService;
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

    private final ProducentService producentService;

    private final UserService userService;

    private final OrdersService ordersService;

    public AppController(ProducentService producentService, UserService userService, OrdersService ordersService) {
        this.producentService = producentService;
        this.userService = userService;
        this.ordersService = ordersService;
    }

    /**
     * Method to display index page
     */
    @GetMapping("")
    public String viewHomePage() {
        return "/shop_pages/index";
    }

    /**
     * Method to display register form page
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());

        return "/login_pages/signup_form";
    }

    /**
     * Method to display products page by given (not used)
     */
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

    /**
     * Method to show page with contact info
     */
    @RequestMapping("/contact")
    public String viewContact(Model model) {

        return "/shop_pages/contact";
    }

    /**
     * Method to show page with partners (producents) info
     */
    @RequestMapping("/partners")
    public String viewPartners(Model model) {
        List<Producent> listProducent = producentService.listAll();
        model.addAttribute("listProducent", listProducent);
        return "/shop_pages/partners";
    }

    /**
     * Method to show login form
     */
    @RequestMapping("/login")
    public String login() {
        return "/login_pages/login_form";
    }

    /**
     * Method to display logged in user's profile
     */
    @RequestMapping("/profile")
    public String viewUserProfile(@AuthenticationPrincipal CustomUserDetails user, Model model) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        model.addAttribute("user", user);
        return "/user_pages/user_profile";
    }

    /**
     * Method to show page with logged in user's orders
     */
    @RequestMapping("/orders")
    public String viewUserOrders(@AuthenticationPrincipal CustomUserDetails loggedUser, Model model) {
        if(loggedUser == null) return "redirect:/app/login";
        User user = getUser(loggedUser);
        List<Orders> orders = ordersService.listByUser(user);
        model.addAttribute("user", user);
        model.addAttribute("orders", orders);
        return "/user_pages/user_orders";
    }

    /**
     * Method to get currently logged in user
     */
    private User getUser(CustomUserDetails loggedUser) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(loggedUser, loggedUser.getPassword(), loggedUser.getAuthorities());
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long id = userDetails.getId();
        return userService.get(id);
    }
}
