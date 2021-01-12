package local.umg.susersmvc.repository;

import local.umg.susersmvc.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Orders, Integer> {
}
