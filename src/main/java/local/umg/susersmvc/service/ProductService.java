package local.umg.susersmvc.service;

import local.umg.susersmvc.model.Category;
import local.umg.susersmvc.model.Producent;
import local.umg.susersmvc.model.Product;
import local.umg.susersmvc.model.Subcategory;
import local.umg.susersmvc.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    public List<Product> listAll() {
        return repository.findAll();
    }

    public void save(String name, String description, int quantity, double price, Category category, Subcategory subcategory, Producent producent, MultipartFile file) {
        System.out.println("Service: save");
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setQuantity(quantity);
        product.setPrice(price);
        product.setCategory(category);
        product.setSubcategory(subcategory);
        product.setProducent(producent);

        try {
            product.setImage(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        repository.save(product);
    }

    public void update(Long id, String name, String description, int quantity, double price, Category category, Subcategory subcategory, Producent producent, MultipartFile file) {
        System.out.println("Service: update");
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setDescription(description);
        product.setQuantity(quantity);
        product.setPrice(price);
        product.setCategory(category);
        product.setSubcategory(subcategory);
        product.setProducent(producent);

        try {
            product.setImage(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

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

    public List<Product> listByCategorySubcategory(Long id, Long ids) {
        return repository.findByCategorySubcategory(id, ids);
    }

    public Page<Product> findPaginated(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return repository.findAll(pageable);
    }
}
