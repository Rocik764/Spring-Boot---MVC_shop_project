package local.umg.susersmvc.repository;

import local.umg.susersmvc.model.Product;
import local.umg.susersmvc.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    public List<Product> findByCategoryId(Long id);

    public List<Product> findBySubcategoryId(Long id);

    @Query("SELECT c FROM Product c WHERE c.category.id = :id AND c.subcategory.id = :ids")
    public List<Product> findByCategorySubcategory(Long id, Long ids);
}
