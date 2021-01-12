package local.umg.susersmvc.controller;

import local.umg.susersmvc.details.CustomUserDetails;
import local.umg.susersmvc.model.CartItem;
import local.umg.susersmvc.model.Orders;
import local.umg.susersmvc.model.User;
import local.umg.susersmvc.service.OrdersService;
import local.umg.susersmvc.service.ShoppingCartServices;
import local.umg.susersmvc.service.UserService;
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

import java.util.List;

@Controller
@RequestMapping("cart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartServices cartServices;

    @Autowired
    private UserService userService;

    @Autowired
    private OrdersService ordersService;

    @GetMapping("/showCart")
    public String showShoppingCart(@AuthenticationPrincipal CustomUserDetails loggedUser, Model model) {
        if(loggedUser == null) return "redirect:/app/login";
        User user = getUser(loggedUser);
        List<CartItem> cartItems = cartServices.listCartItems(user);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("user", user);
        return "/user_pages/shopping_cart";
    }

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

    @RequestMapping(value = "/orderProducts", method = RequestMethod.POST)
    public String orderProducts(@AuthenticationPrincipal CustomUserDetails loggedUser,
                                @RequestParam("delivery") String delivery,
                                @RequestParam("payment") String payment,
                                @RequestParam("address") String address,
                                @RequestParam("code") String code,
                                @RequestParam("city") String city,
                                @RequestParam("phone") String phone,
                                @RequestParam(value = "invoice", required = false) String invoice,
                                @RequestParam(value = "comment", required = false) String comment,
                                @RequestParam("totalPrice") double totalPrice,
                                Model model) {
        System.out.println(delivery + " " + payment + " " + address + " " + code + " " + city + " " + phone + " " + invoice + " " + comment + " " + totalPrice);
        boolean invc = true;
        if(loggedUser == null) return "redirect:/app/login";
        if(invoice == null) invc = false;
        User user = getUser(loggedUser);
        ordersService.orderProducts(user, delivery, payment, address, code, city, phone, invc, comment, totalPrice);
        return "redirect:/app/";
    }

    private User getUser(CustomUserDetails loggedUser) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(loggedUser, loggedUser.getPassword(), loggedUser.getAuthorities());
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long id = userDetails.getId();
        return userService.get(id);
    }
}
