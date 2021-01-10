package local.umg.susersmvc.restController;

import local.umg.susersmvc.details.CustomUserDetails;
import local.umg.susersmvc.model.User;
import local.umg.susersmvc.service.ShoppingCartServices;
import local.umg.susersmvc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("cartRest")
public class ShoppingCartRestController {

    @Autowired
    private ShoppingCartServices cartServices;

    @Autowired
    private UserService userService;

    @PostMapping("/add/{pid}/{amount}")
    @ResponseBody
    public String addProductToCart(@PathVariable("pid") Long productId,
                                   @PathVariable("amount") Integer amount,
                                   @AuthenticationPrincipal CustomUserDetails loggedUser) {
        System.out.println("addProductToCart");
        if(loggedUser == null) {
            return "Musisz się zalogować aby dodać ten produkt do koszyka";
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(loggedUser, loggedUser.getPassword(), loggedUser.getAuthorities());
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long id = userDetails.getId();
        User user = userService.get(id);
        Integer addedAmount = cartServices.addProduct(productId, amount, user);

        return addedAmount + " produkt(ów) zostało dodanych do Twojego koszyka.";
    }

    @PostMapping("/update/{pid}/{amount}")
    @ResponseBody
    public String updateAmount(@PathVariable("pid") Long productId,
                                   @PathVariable("amount") Integer amount,
                                   @AuthenticationPrincipal CustomUserDetails loggedUser) {
        System.out.println("updateAmount: amount: " + amount);
        if(loggedUser == null) {
            return "Musisz się zalogować aby tego użyć.";
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(loggedUser, loggedUser.getPassword(), loggedUser.getAuthorities());
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long id = userDetails.getId();
        User user = userService.get(id);
        double subtotal = cartServices.updateAmount(amount, productId, user);
        System.out.println("updateAmount: subtotal: " + subtotal);
        return String.valueOf(subtotal);
    }

    @PostMapping("/remove/{pid}")
    @ResponseBody
    public String removeProduct(@PathVariable("pid") Long productId,
                               @AuthenticationPrincipal CustomUserDetails loggedUser) {
        System.out.println("removeProduct");
        if(loggedUser == null) {
            return "Musisz się zalogować aby tego użyć.";
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(loggedUser, loggedUser.getPassword(), loggedUser.getAuthorities());
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long id = userDetails.getId();
        User user = userService.get(id);
        cartServices.removeProduct(productId, user);
        return "Produkt został usunięty z koszyka.";
    }
}
