package local.umg.susersmvc.service;

import local.umg.susersmvc.model.Producent;
import local.umg.susersmvc.repository.ProducentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProducentService {

    @Autowired
    private ProducentRepository repository;

    public List<Producent> listAll() {
        return repository.findAll();
    }

    public void save(Producent producent) {
        repository.save(producent);
    }

    public void deleteProducent(Long id) {
        repository.deleteById(id);
    }
}
