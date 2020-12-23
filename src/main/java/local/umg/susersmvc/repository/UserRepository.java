package local.umg.susersmvc.repository;

import local.umg.susersmvc.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByOrderByIdAsc();
    List<User> findAllByLastNameNativeSQL();

    @Query("SELECT u FROM User u WHERE u.email = :email")
    public User findByEmail(String email);
}
