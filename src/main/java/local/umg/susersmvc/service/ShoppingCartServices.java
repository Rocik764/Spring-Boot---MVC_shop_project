package local.umg.susersmvc.service;

import local.umg.susersmvc.model.CartItem;
import local.umg.susersmvc.model.Product;
import local.umg.susersmvc.model.User;
import local.umg.susersmvc.repository.CartItemRepository;
import local.umg.susersmvc.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ShoppingCartServices {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<CartItem> listCartItems(User user) {
        return cartItemRepository.findByUser(user);
    }

    public Integer addProduct(Long productId, Integer amount, User user) {
        Integer addedAmount = amount;
        Product product = productRepository.findById(productId).get();
        CartItem cartItem = cartItemRepository.findByUserAndProduct(user, product);

        if(cartItem != null) {
            addedAmount = cartItem.getAmount() + amount;
            cartItem.setAmount(addedAmount);
        } else {
            cartItem = new CartItem();
            cartItem.setAmount(amount);
            cartItem.setUser(user);
            cartItem.setProduct(product);
        }

        cartItemRepository.save(cartItem);

        return addedAmount;
    }

    public double updateAmount(Integer amount, Long productId, User user) {
        cartItemRepository.updateAmount(amount, productId, user.getId());
        Product product = productRepository.findById(productId).get();
        double subtotal = product.getPrice() * amount;

        return subtotal;
    }

    public void removeProduct(Long productId, User user) {
        cartItemRepository.deleteByUserAndProduct(user.getId(), productId);
    }

    public void deleteByUser(User user) {
        cartItemRepository.deleteByUser(user.getId());
    }
}
