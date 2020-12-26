package local.umg.susersmvc.service;

import local.umg.susersmvc.model.Product;
import local.umg.susersmvc.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    public List<Product> listAll() {
        return repository.findAll();
    }

    public void save(Product product) {
        repository.save(product);
    }

    public Product get(Long id) {
        return repository.findById(id).get();
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<Product> listByCategory(Long id) {
        return repository.findByCategoryId(id);
    }
}
