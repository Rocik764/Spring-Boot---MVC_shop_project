package local.umg.susersmvc.repository;

import local.umg.susersmvc.model.CartItem;
import local.umg.susersmvc.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

    public List<CartItem> findByUser(User user);
}
