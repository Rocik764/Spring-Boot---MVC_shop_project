package local.umg.susersmvc.repository;

import local.umg.susersmvc.model.Producent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

public interface ProducentRepository extends JpaRepository<Producent, Long> {

    @Query("SELECT id FROM Producent")
    public Set<Long> getAllIds();
}
