package local.umg.susersmvc.service;

import local.umg.susersmvc.model.OrderDetails;
import local.umg.susersmvc.repository.OrderDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class OrdersDetailsService {

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    public List<OrderDetails> findAllByUserIdAndDate(Long uId, Date date) {
        return orderDetailsRepository.findAllByUserIdAndPurchase(uId, date);
    }
}
