package local.umg.susersmvc.restController;

import local.umg.susersmvc.details.CustomUserDetails;
import local.umg.susersmvc.model.OrderDetails;
import local.umg.susersmvc.model.User;
import local.umg.susersmvc.service.OrdersDetailsService;
import local.umg.susersmvc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("ordersRest")
public class OrdersDetailsRestController {

    @Autowired
    private OrdersDetailsService ordersDetailsService;

    @Autowired
    private UserService userService;

    /**
     * Method used in AJAX to show modal dialog with detailed order's information
     */
    @GetMapping(value = "/showOrdersDetails/{uId}/{date}")
    public List<OrderDetails> showOrdersDetails(@PathVariable(name = "uId") Long uId, @PathVariable(name = "date") Date date) {
        System.out.println("showOrdersDetails");
        try {
            List<OrderDetails> orderDetails = ordersDetailsService.findAllByUserIdAndDate(uId, date);
            return orderDetails;
        } catch (EntityNotFoundException e) {
            return null;
        }
    }

    @GetMapping(value = "/showOrdersDetailsUser/{date}")
    public List<OrderDetails> showOrdersDetailsUser(@AuthenticationPrincipal CustomUserDetails loggedUser, @PathVariable(name = "date") Date date) {
        System.out.println("showOrdersDetailsUser");
        User uId = getUser(loggedUser);
        try {
            List<OrderDetails> orderDetails = ordersDetailsService.findAllByUserIdAndDate(uId.getId(), date);
            return orderDetails;
        } catch (EntityNotFoundException e) {
            return null;
        }
    }

    private User getUser(CustomUserDetails loggedUser) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(loggedUser, loggedUser.getPassword(), loggedUser.getAuthorities());
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long id = userDetails.getId();
        return userService.get(id);
    }
}
