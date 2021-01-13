package local.umg.susersmvc.service;

import local.umg.susersmvc.details.CustomUserDetails;
import local.umg.susersmvc.model.Role;
import local.umg.susersmvc.model.User;
import local.umg.susersmvc.repository.RoleRepository;
import local.umg.susersmvc.repository.UserRepository;
import org.hibernate.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private RoleRepository roleRepository;

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

    public void editUser(User user) {

    }

    public User get(Long id) {
        return repository.findById(id).get();
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Transactional
    public void editMail(String email, Long id) {
        repository.updateEmail(email, id);
    }

    @Transactional
    public void editFirstName(String first_name, Long id) {
        repository.updateFirstName(first_name, id);
    }

    @Transactional
    public void editLastName(String last_name, Long id) {
        repository.updateLastName(last_name, id);
    }

    @Transactional
    public void editPassword(String password, Long id) {
        repository.updatePassword(password, id);
    }

    public User getCustomerByEmail(String email) {
        return repository.findByEmail(email);
    }

    public void editRoles(Collection<String> roles, Long uId) {
        Set<Role> role = roleRepository.getAllByNameIn(roles);
        User user = repository.findById(uId).get();
        for (Role r : role) {
            System.out.println(r.getId() + " " + r.getName());
        }

        user.setRoles(role);
        repository.save(user);
    }
}
