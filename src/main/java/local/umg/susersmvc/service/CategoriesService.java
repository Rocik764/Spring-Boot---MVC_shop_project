package local.umg.susersmvc.service;

import local.umg.susersmvc.model.Category;
import local.umg.susersmvc.model.Product;
import local.umg.susersmvc.model.Subcategory;
import local.umg.susersmvc.repository.CategoryRepository;
import local.umg.susersmvc.repository.SubcategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriesService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    public List<Category> listAllCategory() {
        return categoryRepository.findAll();
    }

    public List<Subcategory> listAllSubcategory() {
        return subcategoryRepository.findAll();
    }

    public void saveCategory(Category category) {
        categoryRepository.save(category);
    }

    public void saveSubcategory(Subcategory subcategory) {
        subcategoryRepository.save(subcategory);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    public void deleteSubcategory(Long id) {
        subcategoryRepository.deleteById(id);
    }
}
