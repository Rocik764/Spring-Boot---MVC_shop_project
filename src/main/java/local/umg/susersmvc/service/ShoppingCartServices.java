package local.umg.susersmvc.service;

import local.umg.susersmvc.model.CartItem;
import local.umg.susersmvc.model.User;
import local.umg.susersmvc.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingCartServices {

    @Autowired
    private CartItemRepository repository;

    public List<CartItem> listCartItems(User user) {
        return repository.findByUser(user);
    }
}
