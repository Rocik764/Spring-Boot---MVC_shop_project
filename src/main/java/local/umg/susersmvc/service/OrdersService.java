package local.umg.susersmvc.service;

import local.umg.susersmvc.model.*;
import local.umg.susersmvc.repository.CartItemRepository;
import local.umg.susersmvc.repository.OrderDetailsRepository;
import local.umg.susersmvc.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.util.List;

@Service
public class OrdersService {

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private ShoppingCartServices cartServices;

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    public List<Orders> listAll() {
        return ordersRepository.findAll();
    }

    public void orderProducts(User user, String delivery, String payment, String address, String code, String city, String phone, boolean invoice, String comment, double totalPrice) {
        Orders order = new Orders();
        String fullAddress = address + " " + code + " " + city;
        Date date = new Date(System.currentTimeMillis());

        copyCartToDetails(user);
        removeCartItems(user);

        order.setUser(user);
        order.setPurchase_date(date);
        order.setIs_completed(false);
        order.setAddress(fullAddress);
        order.setInvoice(invoice);
        order.setPhone(phone);
        order.setComment(comment);
        order.setDelivery(delivery);
        order.setPayment(payment);
        order.setTotal_price(totalPrice);

        ordersRepository.save(order);
    }

    private void copyCartToDetails(User user) {
        List<CartItem> cartItemList = cartServices.listCartItems(user);

        for (CartItem item : cartItemList) {
            OrderDetails orderDetails = new OrderDetails();
            //orderDetails.setId(item.getId());
            orderDetails.setProduct(item.getProduct());
            orderDetails.setUser(item.getUser());
            orderDetails.setAmount(item.getAmount());
            System.out.println(orderDetails.toString());
            orderDetailsRepository.save(orderDetails);
        }
    }

    private void removeCartItems(User user) {
        cartServices.deleteByUser(user);
    }
}
