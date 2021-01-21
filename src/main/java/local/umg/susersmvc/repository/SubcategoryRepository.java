package local.umg.susersmvc.repository;

import local.umg.susersmvc.model.Subcategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

public interface SubcategoryRepository extends JpaRepository<Subcategory, Long> {

    @Query("SELECT id FROM Subcategory")
    public Set<Long> getAllIds();

    public void deleteById(Long id);
}
