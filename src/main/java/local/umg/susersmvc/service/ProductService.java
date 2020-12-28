package local.umg.susersmvc.service;

import local.umg.susersmvc.model.Product;
import local.umg.susersmvc.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public List<Product> listByCategorySubcategory(Long id, Long ids) {
        return repository.findByCategorySubcategory(id, ids);
    }

    public Page<Product> findPaginated(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return repository.findAll(pageable);
    }
}
