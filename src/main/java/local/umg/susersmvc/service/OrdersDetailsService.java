package local.umg.susersmvc.service;

import local.umg.susersmvc.model.CartItem;
import local.umg.susersmvc.model.OrderDetails;
import local.umg.susersmvc.model.Orders;
import local.umg.susersmvc.model.User;
import local.umg.susersmvc.repository.OrderDetailsRepository;
import local.umg.susersmvc.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class OrdersDetailsService {

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    public List<OrderDetails> findAllByUserId(Long uId) {
        return orderDetailsRepository.findAllByUserId(uId);
    }
}
