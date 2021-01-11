package local.umg.susersmvc.service;

import local.umg.susersmvc.model.Category;
import local.umg.susersmvc.model.Producent;
import local.umg.susersmvc.model.Product;
import local.umg.susersmvc.model.Subcategory;
import local.umg.susersmvc.repository.CategoryRepository;
import local.umg.susersmvc.repository.ProducentRepository;
import local.umg.susersmvc.repository.ProductRepository;
import local.umg.susersmvc.repository.SubcategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    @Autowired
    private ProducentRepository producentRepository;

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

    public Page<Product> listByCategorySubcategoryPaginated(Long id, Long ids, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return repository.findByCategorySubcategoryPaginated(id, ids, pageable);
    }

    public Page<Product> findPaginated(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return repository.findAll(pageable);
    }

    public Page<Product> listBySideFilters(String filterWord, String filterPriceFrom, String filterPriceTo, int pageNo, int pageSize) {
        double from, to;
        try {
            from = Double.parseDouble(filterPriceFrom);
        } catch (NumberFormatException e) {
            from = 0;
        }
        try {
            to = Double.parseDouble(filterPriceTo);
        } catch (NumberFormatException e) {
            to = Double.MAX_VALUE;
        }

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return repository.findWithSideFilteringPaginated(filterWord, from, to, pageable);
    }

    public Page<Product> listByFilters(String filterWord, String filterPriceFrom, String filterPriceTo, String cId, String sId, String pId, int pageNo, int pageSize) {
        double from, to;
        Set<Long> category = new HashSet<>();
        Set<Long> subcategory = new HashSet<>();
        Set<Long> producent = new HashSet<>();

        try {
            from = Double.parseDouble(filterPriceFrom);
        } catch (NumberFormatException e) {
            from = 0;
        }
        try {
            to = Double.parseDouble(filterPriceTo);
        } catch (NumberFormatException e) {
            to = Double.MAX_VALUE;
        }
        try {
            category.add(Long.parseLong(cId));
        } catch (NumberFormatException e) {
            category = categoryRepository.getAllIds();
        }
        try {
            subcategory.add(Long.parseLong(sId));
        } catch (NumberFormatException e) {
            subcategory = subcategoryRepository.getAllIds();
        }
        try {
            producent.add(Long.parseLong(pId));
        } catch (NumberFormatException e) {
            producent = producentRepository.getAllIds();
        }

        System.out.println("listByFilters");
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return repository.findWithFilteringPaginated(filterWord, from, to, category, subcategory, producent, pageable);
    }
}
