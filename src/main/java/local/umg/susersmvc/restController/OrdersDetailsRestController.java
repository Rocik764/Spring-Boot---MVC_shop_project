package local.umg.susersmvc.restController;

import local.umg.susersmvc.model.OrderDetails;
import local.umg.susersmvc.service.OrdersDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("ordersRest")
public class OrdersDetailsRestController {

    @Autowired
    private OrdersDetailsService ordersDetailsService;

    @GetMapping(value = "/showOrdersDetails/{uId}")
    public String addProductToCart(@PathVariable(name = "uId") Long uId) {
        System.out.println("showOrdersDetailsListRest");
        List<OrderDetails> orderDetails = ordersDetailsService.findAllByUserId(uId);
        String listString = orderDetails.stream().map(Object::toString)
                .collect(Collectors.joining(""));
        return listString;
    }
}
