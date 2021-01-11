package local.umg.susersmvc.repository;

import local.umg.susersmvc.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT id FROM Category")
    public Set<Long> getAllIds();
}
