package local.umg.susersmvc.repository;

import local.umg.susersmvc.model.Orders;
import local.umg.susersmvc.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, Integer> {

    public List<Orders> findByUserId(Long uId);

    @Modifying
    @Query("UPDATE Orders o SET o.is_completed = :order WHERE o.id = :id")
    public void completeOrder(Integer id, boolean order);
}
