package local.umg.susersmvc.restController;

import local.umg.susersmvc.details.CustomUserDetails;
import local.umg.susersmvc.model.Product;
import local.umg.susersmvc.model.User;
import local.umg.susersmvc.service.ProductService;
import local.umg.susersmvc.service.ShoppingCartServices;
import local.umg.susersmvc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;


@RestController
@RequestMapping("cartRest")
public class ShoppingCartRestController {

    @Autowired
    private ShoppingCartServices cartServices;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    /**
     * Method used in AJAX to add product to logged-in user's cart and show modal dialog with information
     * if post succeed or not
     */
    @PostMapping("/add/{pid}/{amount}")
    @ResponseBody
    public String addProductToCart(@PathVariable("pid") Long productId,
                                   @PathVariable("amount") Integer amount,
                                   @AuthenticationPrincipal CustomUserDetails loggedUser) {
        System.out.println("addProductToCart");
        if(loggedUser == null) return "In order to add products to the cart, you must be logged in.";

        Product product = productService.get(productId);
        if(amount > product.getQuantity()) return "There's not enough products.";
        if(amount <= 0) return "You cannot add zero or less than zero products to the cart.";

        Authentication authentication = new UsernamePasswordAuthenticationToken(loggedUser, loggedUser.getPassword(), loggedUser.getAuthorities());
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long id = userDetails.getId();
        User user = userService.get(id);
        Integer addedAmount = cartServices.addProduct(productId, amount, user);
        productService.updateQuantity(product.getId(), product.getQuantity() - amount);

        return addedAmount + " products have been added to your cart.";
    }

    /**
     * Method used in AJAX to update product's amount in logged-in user's cart and show modal dialog with information
     * if post succeed or not
     */
    @PostMapping("/update/{pid}/{amount}")
    @ResponseBody
    public String updateAmount(@PathVariable("pid") Long productId,
                                   @PathVariable("amount") Integer amount,
                                   @AuthenticationPrincipal CustomUserDetails loggedUser) {
        System.out.println("updateAmount: amount: " + amount);
        if(loggedUser == null) return "In order to use this, you must be logged in.";

        Product product = productService.get(productId);
        //if(amount > product.getQuantity()) return "Nie możesz dodać do koszyka większej ilości niż jest na stanie.";

        Authentication authentication = new UsernamePasswordAuthenticationToken(loggedUser, loggedUser.getPassword(), loggedUser.getAuthorities());
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long id = userDetails.getId();
        User user = userService.get(id);

        Integer oldQuantity = cartServices.getQuantity(productId, user);
        if(oldQuantity > amount) {
            productService.updateQuantity(productId, product.getQuantity() + 1);
        } else {
            if(product.getQuantity() - 1 < 0) {
                return "There's not enough products.";
            }
            productService.updateQuantity(productId, product.getQuantity() - 1);
        }

        double subtotal = cartServices.updateAmount(amount, productId, user);
        System.out.println("updateAmount: subtotal: " + subtotal);
        return String.valueOf(subtotal);
    }

    /**
     * Method used in AJAX to remove product from logged-in user's cart and show modal dialog with information
     * if post succeed or not
     */
    @PostMapping("/remove/{pid}/{amount}")
    @ResponseBody
    public String removeProduct(@PathVariable("pid") Long productId,
                                @PathVariable("amount") Integer amount,
                               @AuthenticationPrincipal CustomUserDetails loggedUser) {
        System.out.println("removeProduct");
        if(loggedUser == null) return "In order to use this, you must be logged in.";

        Product product = productService.get(productId);
        productService.updateQuantity(product.getId(), product.getQuantity() + amount);
        Authentication authentication = new UsernamePasswordAuthenticationToken(loggedUser, loggedUser.getPassword(), loggedUser.getAuthorities());
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long id = userDetails.getId();
        User user = userService.get(id);
        cartServices.removeProduct(productId, user);
        return "Product has been removed from your cart.";
    }
}
