package local.umg.susersmvc.repository;

import local.umg.susersmvc.model.CartItem;
import local.umg.susersmvc.model.Product;
import local.umg.susersmvc.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

    public List<CartItem> findByUser(User user);

    public CartItem findByUserAndProduct(User user, Product product);

    @Modifying
    @Query("UPDATE CartItem c SET c.amount = ?1 WHERE c.product.id = ?2 AND c.user.id = ?3")
    public void updateAmount(Integer amount, Long productId, Long userId);

    @Modifying
    @Query("DELETE FROM CartItem c WHERE c.user.id = ?1 AND c.product.id = ?2")
    public void deleteByUserAndProduct(Long userId, Long productId);

    @Modifying
    @Query("DELETE FROM CartItem c WHERE c.user.id = ?1")
    public void deleteByUser(Long userId);

    @Query("SELECT c.amount FROM CartItem c WHERE c.user.id = ?2 AND c.product.id = ?1")
    public Integer getQuantity(Long pId, Long uId);
}
