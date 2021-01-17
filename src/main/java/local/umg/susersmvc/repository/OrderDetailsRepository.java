package local.umg.susersmvc.repository;

import local.umg.susersmvc.model.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.List;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Integer> {

    public List<OrderDetails> findAllByUserIdAndPurchase(Long uId, Date date);
}
