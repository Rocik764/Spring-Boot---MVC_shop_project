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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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

    @GetMapping("/listProducts")
    public String viewListProducts(Model model) throws UnsupportedEncodingException {
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
        System.out.println("Controller: new");
        //Product newProduct = new Product();
        List<Category> listCategory = categoriesService.listAllCategory();
        List<Subcategory> listSubcategory = categoriesService.listAllSubcategory();
        List<Producent> listProducent = producentService.listAll();

        //model.addAttribute("product", newProduct);
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
    public String saveProduct(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("quantity") int quantity,
            @RequestParam("price") double price,
            @RequestParam("category") Category category,
            @RequestParam("subcategory") Subcategory subcategory,
            @RequestParam("producent") Producent producent,
            @RequestParam("image") MultipartFile file) {
        System.out.println("Controller: saveProduct");
        service.save(name, description, quantity, price, category, subcategory, producent, file);
        return "redirect:/product/listProducts";
    }

    @RequestMapping(value = "/updateProduct", method = RequestMethod.POST)
    public String updateProduct(
            @RequestParam("id") Long id,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("quantity") int quantity,
            @RequestParam("price") double price,
            @RequestParam("category") Category category,
            @RequestParam("subcategory") Subcategory subcategory,
            @RequestParam("producent") Producent producent,
            @RequestParam("image") MultipartFile file) {
        System.out.println("Controller: saveProduct");
        service.update(id, name, description, quantity, price, category, subcategory, producent, file);
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

    @RequestMapping("/display/{id}")
    void showImage(@PathVariable(name = "id") Long id, HttpServletResponse response) throws ServletException, IOException {
        Product product = service.get(id);
        response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
        response.getOutputStream().write(product.getImage());
        response.getOutputStream().close();
    }

    @RequestMapping("/editProduct/{id}")
    public String showEditFormProduct(@PathVariable(name = "id") Long id, Model model) {
        Product product = service.get(id);
        List<Category> listCategory = categoriesService.listAllCategory();
        List<Subcategory> listSubcategory = categoriesService.listAllSubcategory();
        List<Producent> listProducent = producentService.listAll();
        model.addAttribute("listCategory", listCategory);
        model.addAttribute("listSubcategory", listSubcategory);
        model.addAttribute("listProducent", listProducent);
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
