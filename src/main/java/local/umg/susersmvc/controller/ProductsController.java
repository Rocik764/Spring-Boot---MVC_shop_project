package local.umg.susersmvc.controller;

import local.umg.susersmvc.Pager;
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
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("product")
public class ProductsController {

    private static final int INITIAL_PAGE = 0;

    @Autowired
    private ProductService service;

    @Autowired
    private CategoriesService categoriesService;

    @Autowired
    private ProducentService producentService;

    /**
     * Method to show admin page with products from database
     */
    @GetMapping("/listProducts")
    public String viewListProducts(Model model) {
        List<Product> listProducts = service.listAll();
        model.addAttribute("listProducts", listProducts);
        return "admin_pages/show_products";
    }

    /**
     * Method to show page with products by given category and subcategory - paginated
     */
    @RequestMapping(value = "/category/{Idc}/subcategory/{Idsc}", method = RequestMethod.GET)
    public String viewListProductsByCategory(@PathVariable String Idc,
                                             @PathVariable String Idsc,
                                             @RequestParam("page") Optional<Integer> page,
                                             Model model) {
        Long id = Long.parseLong(Idc);
        Long ids = Long.parseLong(Idsc);
        int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;
        int pageSize = 8;
        Page<Product> products = service.listByCategorySubcategoryPaginated(id, ids, evalPage, pageSize);
        Pager pager = new Pager(products);
        model.addAttribute("productList", products);
        model.addAttribute("pager", pager);
        model.addAttribute("url", "/product/category/{" + Idc + "}/subcategory/{" + Idsc + "}");

        return "/shop_pages/show_product";
    }

    /**
     * Method to show all products - paginated
     */
    @RequestMapping("/page")
    public String showProducts(@RequestParam("page") Optional<Integer> page, Model model) {
        int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;
        int pageSize = 8;
        Page<Product> products = service.findPaginated(evalPage, pageSize);
        Pager pager = new Pager(products);
        //List<Product> productList = page.getContent();
//        model.addAttribute("currentPage", pageNo);
//        model.addAttribute("totalPages", page.getTotalPages());
//        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("productList", products);
        model.addAttribute("pager", pager);
        model.addAttribute("url", "/product/page");
        return "/shop_pages/show_product";
    }

    /**
     * Method to show admin page with a form for a new product
     */
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

    /**
     * Method to show page with 2 forms to create new category and subcategory
     */
    @RequestMapping("/newCategory")
    public String showNewCategory(Model model) {
        Category category = new Category();
        Subcategory subcategory = new Subcategory();
        model.addAttribute("category", category);
        model.addAttribute("subcategory", subcategory);
        return "/admin_pages/new_category_subcategory";
    }

    /**
     * Post method to create new product and save it in database
     */
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

    /**
     * Post method to update chosen product
     */
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

    /**
     * Post method to save new category
     */
    @RequestMapping(value = "/saveCategory", method = RequestMethod.POST)
    public String saveCategory(@ModelAttribute("category") Category category) {
        categoriesService.saveCategory(category);
        return "redirect:/product/listProducts";
    }

    /**
     * Post method to save new subcategory
     */
    @RequestMapping(value = "/saveSubcategory", method = RequestMethod.POST)
    public String saveSubcategory(@ModelAttribute("subcategory") Subcategory subcategory) {
        categoriesService.saveSubcategory(subcategory);
        return "redirect:/product/listProducts";
    }

    /**
     * Method to convert image from database and display it in img tag
     */
    @RequestMapping("/display/{id}")
    void showImage(@PathVariable(name = "id") Long id, HttpServletResponse response) throws ServletException, IOException {
        Product product = service.get(id);
        response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
        response.getOutputStream().write(product.getImage());
        response.getOutputStream().close();
    }

    /**
     * Method to show edit product admin page with a form
     */
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

    /**
     * Method to show page with chosen product's information
     */
    @RequestMapping("/productInfo/{id}")
    public String showProductInfo(@PathVariable(name = "id") Long id, Model model) {
        Product product = service.get(id);
        model.addAttribute("product", product);
        return "/shop_pages/product_info";
    }

    /**
     * Method to delete chosen product from database
     */
    @RequestMapping("/deleteProduct/{id}")
    public String deleteProduct(@PathVariable(name = "id") Long id) {
        service.delete(id);
        return "redirect:/listProducts";
    }

    /**
     * Method to show detailed-filtering page
     */
    @RequestMapping("/filtering")
    public String showFilteringForm(Model model) {
        List<Category> listCategory = categoriesService.listAllCategory();
        List<Subcategory> listSubcategory = categoriesService.listAllSubcategory();
        List<Producent> listProducent = producentService.listAll();
        model.addAttribute("listCategory", listCategory);
        model.addAttribute("listSubcategory", listSubcategory);
        model.addAttribute("listProducent", listProducent);
        return "/shop_pages/filtering";
    }

    /**
     * Method to filter products by side_menu fragment
     */
    @RequestMapping(value = "/startSideMenuFiltering", method = { RequestMethod.GET, RequestMethod.POST })
    public String startSideMenuFiltering(
            @RequestParam("filterWord") String filterWord,
            @RequestParam("filterPriceFrom") String filterPriceFrom,
            @RequestParam("filterPriceTo") String filterPriceTo,
            @RequestParam("page") Optional<Integer> page,
            Model model) {

        int pageSize = 8;
        int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;
        Page<Product> products = service.listBySideFilters(filterWord, filterPriceFrom, filterPriceTo, evalPage, pageSize);
        Pager pager = new Pager(products);

        model.addAttribute("productList", products);
        model.addAttribute("pager", pager);
        model.addAttribute("url", "/product/startSideMenuFiltering?filterWord=" + filterWord
                + "&filterPriceFrom=" + filterPriceFrom
                + "&filterPriceTo=" + filterPriceTo);

        return "/shop_pages/show_product";
    }

    /**
     * Method to filter products by detailed-filtering page
     */
    @RequestMapping(value = "/startFiltering", method = { RequestMethod.GET, RequestMethod.POST })
    public String startFiltering(
            @RequestParam("filterWord") String filterWord,
            @RequestParam("filterPriceFrom") String filterPriceFrom,
            @RequestParam("filterPriceTo") String filterPriceTo,
            @RequestParam("category") String cId,
            @RequestParam("subcategory") String sId,
            @RequestParam("producent") String pId,
            @RequestParam("page") Optional<Integer> page,
            Model model) {

        int pageSize = 8;
        int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;
        Page<Product> products = service.listByFilters(filterWord, filterPriceFrom, filterPriceTo, cId, sId, pId, evalPage, pageSize);
        Pager pager = new Pager(products);

        model.addAttribute("productList", products);
        model.addAttribute("pager", pager);
        model.addAttribute("url", "/product/startFiltering?filterWord=" + filterWord
                + "&filterPriceFrom=" + filterPriceFrom
                + "&filterPriceTo=" + filterPriceTo
                + "&category=" + cId
                + "&subcategory=" + sId
                + "&producent=" + pId);

        return "/shop_pages/show_product";
    }
}
