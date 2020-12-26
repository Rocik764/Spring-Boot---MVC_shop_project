package local.umg.susersmvc.controller;

import local.umg.susersmvc.model.Category;
import local.umg.susersmvc.model.Product;
import local.umg.susersmvc.model.User;
import local.umg.susersmvc.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @RequestMapping("/listProducts")
    public String viewListProducts(Model model) {
        List<Product> listProducts = service.listAll();
        model.addAttribute("listProducts", listProducts);
        return "shop_pages/show_products";
    }

    @RequestMapping("/category/{Idc}")
    public String viewListProductsByCategory(@PathVariable String Idc, Model model) {
        Long Id = Long.parseLong(Idc);
        List<Product> listProducts = service.listByCategory(Id);
        model.addAttribute("listProducts", listProducts);
        return "shop_pages/show_products";
    }

    @RequestMapping("/new")
    public String showNewFormProduct(Model model) {
        Product newProduct = new Product();
        model.addAttribute("product", newProduct);
        return "/admin_pages/new_product";
    }

    @RequestMapping(value = "/saveProduct", method = RequestMethod.POST)
    public String saveProduct(@ModelAttribute("product") Product product) {
        service.save(product);
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
}
