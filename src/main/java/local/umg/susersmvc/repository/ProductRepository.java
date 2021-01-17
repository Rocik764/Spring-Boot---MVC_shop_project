package local.umg.susersmvc.repository;

import local.umg.susersmvc.model.Category;
import local.umg.susersmvc.model.Producent;
import local.umg.susersmvc.model.Product;
import local.umg.susersmvc.model.Subcategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface ProductRepository extends JpaRepository<Product, Long> {

    public List<Product> findByCategoryId(Long id);

    public List<Product> findBySubcategoryId(Long id);

    @Query("SELECT c FROM Product c WHERE c.category.id = :id AND c.subcategory.id = :ids")
    public List<Product> findByCategorySubcategory(Long id, Long ids);

    @Query("SELECT c FROM Product c WHERE c.category.id = :id AND c.subcategory.id = :ids")
    public Page<Product> findByCategorySubcategoryPaginated(Long id, Long ids, Pageable pageable);

    @Query("SELECT c FROM Product c WHERE c.name LIKE %?1% AND c.price > ?2 AND c.price < ?3")
    public Page<Product> findWithSideFilteringPaginated(String filterWord, double filterPriceFrom, double filterPriceTo, Pageable pageable);

    @Query("SELECT c FROM Product c WHERE c.name LIKE %:filterWord% " +
            "AND c.price BETWEEN :filterPriceFrom AND :filterPriceTo " +
            "AND c.category.id IN :cId " +
            "AND c.subcategory.id IN :sId " +
            "AND c.producent.id IN :pId")
    public Page<Product> findWithFilteringPaginated(String filterWord, double filterPriceFrom, double filterPriceTo, Set<Long> cId, Set<Long> sId, Set<Long> pId, Pageable pageable);

    @Modifying
    @Query("UPDATE Product c SET c.quantity = ?2 WHERE c.id = ?1")
    public void updateQuantity(Long pId, Integer quantity);
}
