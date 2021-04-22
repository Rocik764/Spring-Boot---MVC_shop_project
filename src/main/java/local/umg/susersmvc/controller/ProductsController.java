package local.umg.susersmvc.controller;

import local.umg.susersmvc.Pager;
import local.umg.susersmvc.model.*;
import local.umg.susersmvc.service.CategoriesService;
import local.umg.susersmvc.service.ProducentService;
import local.umg.susersmvc.service.ProductService;
import local.umg.susersmvc.validate.ProductValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("product")
public class ProductsController {

    private static final int INITIAL_PAGE = 0;

    private final ProductService service;

    private final CategoriesService categoriesService;

    private final ProducentService producentService;

    public ProductsController(ProductService service, CategoriesService categoriesService, ProducentService producentService) {
        this.service = service;
        this.categoriesService = categoriesService;
        this.producentService = producentService;
    }

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
        getCategoryList(model);

        return "/admin_pages/new_product";
    }

    /**
     * Method to show page with 3 forms to create new category, subcategory and producent
     */
    @GetMapping("/newCategory")
    public String showNewCategory(Model model) {
        Category category = new Category();
        Subcategory subcategory = new Subcategory();
        Producent producent = new Producent();
        if(!model.containsAttribute("category")) model.addAttribute("category", category);
        if(!model.containsAttribute("subcategory")) model.addAttribute("subcategory", subcategory);
        if(!model.containsAttribute("producent")) model.addAttribute("producent", producent);
        return "/admin_pages/new_category_subcategory";
    }

    /**
     * Post method to create new product and save it in database
     */
    @RequestMapping(value = "/saveProduct", method = RequestMethod.POST)
    public String saveProduct(@RequestParam("name") String name,
                              @RequestParam("description") String description,
                              @RequestParam("quantity") String quantity,
                              @RequestParam("price") String price,
                              @RequestParam(value = "category", required = false) Category category,
                              @RequestParam(value = "subcategory", required = false) Subcategory subcategory,
                              @RequestParam(value = "producent", required = false) Producent producent,
                              @RequestParam("image") MultipartFile file,
                              RedirectAttributes redirectAttributes) {
        System.out.println(category);
        ProductValidation productValidation = new ProductValidation(
                name,
                description,
                quantity,
                price,
                category,
                subcategory,
                producent,
                categoriesService.listAllCategory(),
                categoriesService.listAllSubcategory(),
                producentService.listAll()
        );

        if(productValidation.validate()) {
            int validQuantity = productValidation.getParsedQuantity();
            double validPrice = productValidation.getParsedPrice();
            service.save(name, description, validQuantity, validPrice, category, subcategory, producent, file);
        } else {
            redirectAttributes.addFlashAttribute("errors", productValidation.getErrors());
            return "redirect:/product/new";
        }

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
            @RequestParam("quantity") String quantity,
            @RequestParam("price") String price,
            @RequestParam(value = "category", required = false) Category category,
            @RequestParam(value = "subcategory", required = false) Subcategory subcategory,
            @RequestParam(value = "producent", required = false) Producent producent,
            @RequestParam("image") MultipartFile file,
            RedirectAttributes redirectAttributes) {

        ProductValidation productValidation = new ProductValidation(
                name,
                description,
                quantity,
                price,
                category,
                subcategory,
                producent,
                categoriesService.listAllCategory(),
                categoriesService.listAllSubcategory(),
                producentService.listAll()
        );

        if(productValidation.validate()) {
            int validQuantity = productValidation.getParsedQuantity();
            double validPrice = productValidation.getParsedPrice();
            service.update(id, name, description, validQuantity, validPrice, category, subcategory, producent, file);
        } else {
            redirectAttributes.addFlashAttribute("errors", productValidation.getErrors());
            return "redirect:/product/editProduct/" + id;
        }


        return "redirect:/product/listProducts";
    }

    /**
     * Post method to save new category
     */
    @PostMapping(value = "/saveCategory")
    public String saveCategory(@Valid @ModelAttribute("category") Category category,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.category", bindingResult);
            redirectAttributes.addFlashAttribute("category", category);
            return "redirect:/product/newCategory";
        }

        try {
            categoriesService.saveCategory(category);
            redirectAttributes.addFlashAttribute("success", "Saved new category.");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error", "This category already exists.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.toString());
        }

        return "redirect:/product/newCategory";
    }

    /**
     * Post method to save new subcategory
     */
    @PostMapping(value = "/saveSubcategory")
    public String saveSubcategory(@Valid @ModelAttribute("subcategory") Subcategory subcategory,
                                  BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.subcategory", bindingResult);
            redirectAttributes.addFlashAttribute("subcategory", subcategory);
            return "redirect:/product/newCategory";
        }

        try {
            categoriesService.saveSubcategory(subcategory);
            redirectAttributes.addFlashAttribute("success", "Saved new subcategory.");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error", "This subcategory already exists.");
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            redirectAttributes.addFlashAttribute("error", e.toString() + "\n" + exceptionAsString);
        }
        return "redirect:/product/newCategory";
    }

    @PostMapping(value = "/saveProducent")
    public String saveProducent(@Valid @ModelAttribute("producent") Producent producent,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes
                                ) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.producent", bindingResult);
            redirectAttributes.addFlashAttribute("producent", producent);
            return "redirect:/product/newCategory";
        }

        try {
            producentService.save(producent);
            redirectAttributes.addFlashAttribute("success", "Saved new producent.");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error", "This producent already exists.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.toString());
        }
        return "redirect:/product/newCategory";
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
        getCategoryList(model);
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

        return "redirect:/product/listProducts";
    }

    /**
     * Method to show detailed-filtering page
     */
    @RequestMapping("/filtering")
    public String showFilteringForm(Model model) {
        getCategoryList(model);

        return "/shop_pages/filtering";
    }

    private void getCategoryList(Model model) {
        List<Category> listCategory = categoriesService.listAllCategory();
        List<Subcategory> listSubcategory = categoriesService.listAllSubcategory();
        List<Producent> listProducent = producentService.listAll();
        model.addAttribute("listCategory", listCategory);
        model.addAttribute("listSubcategory", listSubcategory);
        model.addAttribute("listProducent", listProducent);
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

    @GetMapping("/deleteCategories")
    public String deleteCategory(Model model) {
        getCategoryList(model);
        return "admin_pages/edit_category_subcategory";
    }

    @RequestMapping("/deleteCategory/{id}")
    public String deleteCategory(@PathVariable(name = "id") Long id, RedirectAttributes redirectAttributes) {
        try {
            categoriesService.deleteCategory(id);
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error", "" + "Removing this category will cause data integrity because there are products associated with this category.");
        } catch (Exception e) {
            System.out.println(e.toString());
            redirectAttributes.addFlashAttribute("error", "Error while removing category.");
        }

        return "redirect:/product/deleteCategories";
    }

    @RequestMapping("/deleteSubcategory/{id}")
    public String deleteSubcategory(@PathVariable(name = "id") Long id, RedirectAttributes redirectAttributes) {
        try {
            categoriesService.deleteSubcategory(id);
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error", "Removing this subcategory will cause data integrity because there are products associated with this subcategory.");
        } catch (Exception e) {
            System.out.println(e.toString());
            redirectAttributes.addFlashAttribute("error", "Error while removing subcategory.");
        }

        return "redirect:/product/deleteCategories";
    }

    @RequestMapping("/deleteProducent/{id}")
    public String deleteProducent(@PathVariable(name = "id") Long id, RedirectAttributes redirectAttributes) {
        try {
            producentService.deleteProducent(id);
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error", "Removing this producent will cause data integrity because there are products associated with this producent.");
        } catch (Exception e) {
            System.out.println(e.toString());
            redirectAttributes.addFlashAttribute("error", "Error while removing producent.");
        }

        return "redirect:/product/deleteCategories";
    }
}
