package local.umg.susersmvc.restController;

import local.umg.susersmvc.model.OrderDetails;
import local.umg.susersmvc.service.OrdersDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("ordersRest")
public class OrdersDetailsRestController {

    @Autowired
    private OrdersDetailsService ordersDetailsService;

    /**
     * Method used in AJAX to show modal dialog with detailed order's information
     */
    @GetMapping(value = "/showOrdersDetails/{uId}/{date}")
    public String addProductToCart(@PathVariable(name = "uId") Long uId, @PathVariable(name = "date") Date date) {
        System.out.println("showOrdersDetailsListRest");
        List<OrderDetails> orderDetails = ordersDetailsService.findAllByUserIdAndDate(uId, date);
        String listString = orderDetails.stream().map(Object::toString)
                .collect(Collectors.joining(""));
        return listString;
    }
}
