package local.umg.susersmvc.controller;

import local.umg.susersmvc.model.*;
import local.umg.susersmvc.service.CategoriesService;
import local.umg.susersmvc.service.ProducentService;
import local.umg.susersmvc.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.PrinterURI;
import java.util.List;

@Controller
@RequestMapping("product")
public class ProductsController {

    @Autowired
    private ProductService service;

    @Autowired
    private CategoriesService categoriesService;

    @Autowired
    private ProducentService producentService;

    @RequestMapping("/listProducts")
    public String viewListProducts(Model model) {
        List<Product> listProducts = service.listAll();
        model.addAttribute("listProducts", listProducts);
        return "shop_pages/show_products";
    }

    @RequestMapping("/category/{Idc}/subcategory/{Idsc}")
    public String viewListProductsByCategory(@PathVariable String Idc, @PathVariable String Idsc, Model model) {
        Long id = Long.parseLong(Idc);
        Long ids = Long.parseLong(Idsc);
        List<Product> listProducts = service.listByCategorySubcategory(id, ids);
        model.addAttribute("listProducts", listProducts);
        return "shop_pages/show_products";
    }

    @RequestMapping("/new")
    public String showNewFormProduct(Model model) {
        Product newProduct = new Product();
        List<Category> listCategory = categoriesService.listAllCategory();
        List<Subcategory> listSubcategory = categoriesService.listAllSubcategory();
        List<Producent> listProducent = producentService.listAll();

        model.addAttribute("product", newProduct);
        model.addAttribute("listCategory", listCategory);
        model.addAttribute("listSubcategory", listSubcategory);
        model.addAttribute("listProducent", listProducent);

        return "/admin_pages/new_product";
    }

    @RequestMapping("/newCategory")
    public String showNewCategory(Model model) {
        Category category = new Category();
        Subcategory subcategory = new Subcategory();
        model.addAttribute("category", category);
        model.addAttribute("subcategory", subcategory);
        return "/admin_pages/new_category_subcategory";
    }

    @RequestMapping(value = "/saveProduct", method = RequestMethod.POST)
    public String saveProduct(@ModelAttribute("product") Product product) {
        service.save(product);
        return "redirect:/product/listProducts";
    }

    @RequestMapping(value = "/saveCategory", method = RequestMethod.POST)
    public String saveCategory(@ModelAttribute("category") Category category) {
        categoriesService.saveCategory(category);
        return "redirect:/product/listProducts";
    }

    @RequestMapping(value = "/saveSubcategory", method = RequestMethod.POST)
    public String saveSubcategory(@ModelAttribute("subcategory") Subcategory subcategory) {
        categoriesService.saveSubcategory(subcategory);
        return "redirect:/product/listProducts";
    }

    @RequestMapping("/editProduct/{id}")
    public String showEditFormProduct(@PathVariable(name = "id") Long id, Model model) {
        Product product = service.get(id);
        model.addAttribute("product", product);
        return "/admin_pages/edit_product";
    }

    @RequestMapping("/deleteProduct/{id}")
    public String deleteProduct(@PathVariable(name = "id") Long id) {
        service.delete(id);
        return "redirect:/listProducts";
    }

    @RequestMapping("/page/{pageNo}")
    public String deleteProduct(@PathVariable(name = "pageNo") int pageNo, Model model) {
        if(pageNo <= 0) pageNo = 1;
        int pageSize = 6;
        Page<Product> page = service.findPaginated(pageNo, pageSize);
        List<Product> productList = page.getContent();
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("productList", productList);
        return "/shop_pages/show_product";
    }
}
