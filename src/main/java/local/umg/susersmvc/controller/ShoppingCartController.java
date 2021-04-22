package local.umg.susersmvc.controller;

import local.umg.susersmvc.details.CustomUserDetails;
import local.umg.susersmvc.model.CartItem;
import local.umg.susersmvc.model.Orders;
import local.umg.susersmvc.model.User;
import local.umg.susersmvc.service.OrdersService;
import local.umg.susersmvc.service.ShoppingCartServices;
import local.umg.susersmvc.service.UserService;
import local.umg.susersmvc.validate.OrderValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Controller
@RequestMapping("cart")
public class ShoppingCartController {

    private final ShoppingCartServices cartServices;

    private final UserService userService;

    private final OrdersService ordersService;

    public ShoppingCartController(OrdersService ordersService, ShoppingCartServices cartServices, UserService userService) {
        this.ordersService = ordersService;
        this.cartServices = cartServices;
        this.userService = userService;
    }

    /**
     * Method to show logged-in user's shopping cart page
     */
    @GetMapping("/showCart")
    public String showShoppingCart(@AuthenticationPrincipal CustomUserDetails loggedUser, Model model) {
        if(loggedUser == null) return "redirect:/app/login";
        User user = getUser(loggedUser);
        List<CartItem> cartItems = cartServices.listCartItems(user);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("user", user);
        return "/user_pages/shopping_cart";
    }

    /**
     * Method to show page with form to submit order.
     */
    @GetMapping("/showOrderDetails")
    public String showOrderDetails(@AuthenticationPrincipal CustomUserDetails loggedUser, Model model) {
        if(loggedUser == null) return "redirect:/app/login";
        User user = getUser(loggedUser);
        List<CartItem> cartItems = cartServices.listCartItems(user);
        if(cartItems.isEmpty()) return "redirect:/cart/showCart";
        else {
            double totalPrice = 0;
            for (CartItem item : cartItems) totalPrice += item.getSubtotal();
            model.addAttribute("totalPrice", totalPrice);
        }
        Orders order = new Orders();
        model.addAttribute("order", order);
        model.addAttribute("user", user);
        return "/user_pages/order_details";
    }

    /**
     * Method to submit orders from logged-in user's shopping cart
     */
    @RequestMapping(value = "/orderProducts", method = RequestMethod.POST)
    public String orderProducts(@AuthenticationPrincipal CustomUserDetails loggedUser,
                                @RequestParam(value = "delivery", defaultValue = "") String delivery,
                                @RequestParam(value = "payment", defaultValue = "") String payment,
                                @RequestParam(value = "address") String address,
                                @RequestParam(value = "code") String code,
                                @RequestParam(value = "city") String city,
                                @RequestParam(value = "phone") String phone,
                                @RequestParam(value = "invoice", required = false) String invoice,
                                @RequestParam(value = "comment", required = false) String comment,
                                @RequestParam(value = "regulations", required = false) String regulations,
                                RedirectAttributes redirectAttributes) {
        System.out.println(regulations);
        if(loggedUser == null) return "redirect:/app/login";
        OrderValidation orderValidation = new OrderValidation(delivery, payment, address, code, city, phone, regulations);

        if(orderValidation.validate()) {
            boolean invc = true;

            if(invoice == null) invc = false;
            User user = getUser(loggedUser);

            List<CartItem> cartItems = cartServices.listCartItems(user);
            double totalPrice = 0;
            for (CartItem item : cartItems) totalPrice += item.getSubtotal();

            ordersService.orderProducts(user, delivery, payment, address, code, city, phone, invc, comment, totalPrice);
        } else {
            redirectAttributes.addFlashAttribute("errors", orderValidation.getErrors());
            return "redirect:/cart/showOrderDetails";
        }

        return "redirect:/cart/showCart";
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
