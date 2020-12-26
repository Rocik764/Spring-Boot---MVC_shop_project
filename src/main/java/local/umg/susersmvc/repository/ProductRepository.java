package local.umg.susersmvc.repository;

import local.umg.susersmvc.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    public List<Product> findByCategoryId(Long id);
}
