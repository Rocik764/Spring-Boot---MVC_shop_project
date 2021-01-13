package local.umg.susersmvc.repository;

import local.umg.susersmvc.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Set;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    public Set<Role> getAllByNameIn(Collection<String> roles);
}
