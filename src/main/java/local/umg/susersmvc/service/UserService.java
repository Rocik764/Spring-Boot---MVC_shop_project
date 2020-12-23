package local.umg.susersmvc.service;

import local.umg.susersmvc.model.User;
import local.umg.susersmvc.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public List<User> listAll() {
        return repository.findAllByLastNameNativeSQL();
    }

    public List<User> listAllAsc() {
        return repository.findByOrderByIdAsc();
    }

    public void save(User user) {
        repository.save(user);
    }

    public User get(Long id) {
        return repository.findById(id).get();
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
