package local.umg.susersmvc.repository;

import local.umg.susersmvc.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByOrderByIdAsc();
    List<User> findAllByLastNameNativeSQL();

    @Query("SELECT u FROM User u WHERE u.email = :email")
    public User findByEmail(String email);

    @Modifying
    @Query("UPDATE User u SET u.email = :email WHERE u.id = :id")
    public void updateEmail(String email, Long id);

    @Modifying
    @Query("UPDATE User u SET u.first_name = :first_name WHERE u.id = :id")
    public void updateFirstName(@Param(value = "first_name") String first_name, @Param(value = "id") Long id);

    @Modifying
    @Query("UPDATE User u SET u.last_name = :last_name WHERE u.id = :id")
    public void updateLastName(@Param(value = "last_name") String last_name, @Param(value = "id") Long id);

    @Modifying
    @Query("UPDATE User u SET u.password = :password WHERE u.id = :id")
    public void updatePassword(@Param(value = "password") String password, @Param(value = "id") Long id);
}
