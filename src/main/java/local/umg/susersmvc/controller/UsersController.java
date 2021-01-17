package local.umg.susersmvc.controller;

import local.umg.susersmvc.details.CustomUserDetails;
import local.umg.susersmvc.model.User;
import local.umg.susersmvc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("users")
public class UsersController {

    @Autowired
    private UserService service;

    /**
     * Method to save new user from registration form
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String saveUser(@ModelAttribute("user") User user) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        service.save(user);

        return "/login_pages/register_success";
    }

    /**
     * Method for logged-in user to edit his own mail
     */
    @RequestMapping(value = "/editMail", method = RequestMethod.POST)
    public String editUserMail(Authentication authentication, @RequestParam("email") String email) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long id = userDetails.getId();
        System.out.println(email + " : " + id);
        service.editMail(email, id);

        return "redirect:/app/profile";
    }

    /**
     * Method for logged-in user to edit his own first name
     */
    @RequestMapping(value = "/editFirstName", method = RequestMethod.POST)
    public String editUserFirstName(Authentication authentication, @RequestParam("first_name") String first_name) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long id = userDetails.getId();
        System.out.println(first_name + " : " + id);
        service.editFirstName(first_name, id);

        return "redirect:/app/profile";
    }

    /**
     * Method for logged-in user to edit his own last name
     */
    @RequestMapping(value = "/editLastName", method = RequestMethod.POST)
    public String editUserLastName(Authentication authentication, @RequestParam("last_name") String last_name) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long id = userDetails.getId();
        System.out.println(last_name + " : " + id);
        service.editLastName(last_name, id);

        return "redirect:/app/profile";
    }

    /**
     * Method for logged-in user to edit his own password
     */
    @RequestMapping(value = "/editPassword", method = RequestMethod.POST)
    public String editUserPassword(Authentication authentication, @RequestParam("password") String password) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long id = userDetails.getId();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(password);
        System.out.println(encodedPassword + " : " + id);
        service.editPassword(encodedPassword, id);

        return "redirect:/app/profile";
    }
}
