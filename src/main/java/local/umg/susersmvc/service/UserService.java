package local.umg.susersmvc.service;

import local.umg.susersmvc.model.Role;
import local.umg.susersmvc.model.User;
import local.umg.susersmvc.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private EntityManager entityManager;

    public List<User> listAll() {
        return repository.findAllByLastNameNativeSQL();
    }

    public List<User> listAllAsc() {
        return repository.findByOrderByIdAsc();
    }

    public void save(User user) {
        Role userRole = entityManager.find(Role.class, 2);
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);
        repository.save(user);
    }

    public User get(Long id) {
        return repository.findById(id).get();
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
