package local.umg.susersmvc.controller;

import local.umg.susersmvc.model.Orders;
import local.umg.susersmvc.model.Role;
import local.umg.susersmvc.model.User;
import local.umg.susersmvc.service.OrdersService;
import local.umg.susersmvc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping("admin")
public class AdminController {

    @Autowired
    private UserService service;

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private EntityManager entityManager;

    /**
     * Method to list all users
     */
    @RequestMapping("/listUsers")
    public String viewListUsers(Model model) {

        List<User> listUsers = service.listAll();
        model.addAttribute("listUsers", listUsers);
        return "/admin_pages/users_list";
    }

    /**
     * Test method to show test form of adding new user through admin panel
     */
    @RequestMapping("/new")
    public String showNewFormUser(Model model) {
        User nuser = new User();
        model.addAttribute("user", nuser);
        return "/admin_pages/new_user";
    }

    /**
     * Method to save edited user from form
     */
    @RequestMapping(value = "/editUser", method = RequestMethod.POST)
    public String editUser(@ModelAttribute("user") User user, Model model) {
        service.editUser(user);
        return "redirect:/admin/listUsers";
    }

    /**
     * Method to display page with user editing form
     */
    @RequestMapping("/edit/{id}")
    public String showEditFormUser(@PathVariable(name = "id") Long id, Model model) {
        //ModelAndView mav = new ModelAndView("admin_pages/edit_user");
        User euser = service.get(id);
        List<Role> roles = entityManager.createQuery("SELECT a FROM Role a", Role.class).getResultList();
        model.addAttribute("roles", roles);
        model.addAttribute("user", euser);
        return "/admin_pages/edit_user";
    }

    /**
     * Method to delete chosen user
     */
    @RequestMapping("/delete/{id}")
    public String deleteUser(@PathVariable(name = "id") Long id) {
        service.delete(id);
        return "redirect:/admin/listUsers";
    }

    /**
     * Single user mail reset method for chosen user from admin user-edit panel
     */
    @RequestMapping(value = "/editMail", method = RequestMethod.POST)
    public String editUserMail(@ModelAttribute("user") User user) {
        Long id = user.getId();
        String email = user.getEmail();
        System.out.println(email + " : " + id);
        service.editMail(email, id);

        return "redirect:/admin/listUsers";
    }

    /**
     * Single user first name reset method for chosen user from admin user-edit panel
     */
    @RequestMapping(value = "/editFirstName", method = RequestMethod.POST)
    public String editUserFirstName(@ModelAttribute("user") User user) {
        Long id = user.getId();
        String first_name = user.getFirst_name();
        System.out.println(first_name + " : " + id);
        service.editFirstName(first_name, id);

        return "redirect:/admin/listUsers";
    }

    /**
     * Single user last name reset method for chosen user from admin user-edit panel
     */
    @RequestMapping(value = "/editLastName", method = RequestMethod.POST)
    public String editUserLastName(@ModelAttribute("user") User user) {
        Long id = user.getId();
        String last_name = user.getLast_name();
        System.out.println(user.getLast_name() + " : " + id);
        service.editLastName(last_name, id);

        return "redirect:/admin/listUsers";
    }

    /**
     * Single user password reset method for chosen user from admin user-edit panel
     */
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

    /**
     * Gets array of selected roles from admin user-edit panel and sets them as chosen user's new roles
     */
    @RequestMapping(value = "/editRoles/{uId}", method = RequestMethod.POST)
    public String editUserRoles(@RequestParam("role")String[] roles, @PathVariable(value = "uId") Long uId) {
        Collection<String> roleCollection = new ArrayList<>();
        for (String role : roles) {
            System.out.println(role);
            roleCollection.add(role);
        }
        service.editRoles(roleCollection, uId);
        return "redirect:/admin/listUsers";
    }

    /**
     * Test method to create new user from admin panel
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String saveUser(@ModelAttribute("user") User user) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        service.save(user);

        return "redirect:/admin/listUsers";
    }

    /**
     * Method shows every orders in a table
     */
    @RequestMapping("/showOrders")
    public String showOrdersList(Model model) {
        List<Orders> orders = ordersService.listAll();
        model.addAttribute("orders", orders);

        return "/admin_pages/orders_list";
    }

    /**
     * Method to complete given users order from admin/employee panel
     */
    @RequestMapping("/completeOrder/{id}")
    public String completeOrder(@PathVariable(value = "id") Integer id) {
        ordersService.completeOrder(id);
        return "redirect:/admin/showOrders";
    }
}
