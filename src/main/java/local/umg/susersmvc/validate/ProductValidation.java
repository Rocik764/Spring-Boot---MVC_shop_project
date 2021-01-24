package local.umg.susersmvc.validate;

import local.umg.susersmvc.model.Category;
import local.umg.susersmvc.model.Producent;
import local.umg.susersmvc.model.Subcategory;

import java.util.ArrayList;
import java.util.List;

public class ProductValidation {

    private String name;
    private String description;
    private String quantity;
    private String price;
    private Category category;
    private Subcategory subcategory;
    private Producent producent;
    List<Category> listCategory;
    List<Subcategory> listSubcategory;
    List<Producent> listProducent;

    private List<String> errors = new ArrayList<>();
    private int parsedQuantity;
    private double parsedPrice;

    public ProductValidation(String name, String description, String quantity, String price, Category category, Subcategory subcategory, Producent producent, List<Category> listCategory, List<Subcategory> listSubcategory, List<Producent> listProducent) {
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.category = category;
        this.subcategory = subcategory;
        this.producent = producent;
        this.listCategory = listCategory;
        this.listSubcategory = listSubcategory;
        this.listProducent = listProducent;
    }

    public List<String> getErrors() {
        return this.errors;
    }

    public int getParsedQuantity() {
        return parsedQuantity;
    }

    public double getParsedPrice() {
        return parsedPrice;
    }

    public boolean validate() {

        boolean result = true;

        if(!validName()) result = false;
        if(!validDescription()) result = false;
        if(!validQuantity()) result = false;
        if(!validPrice()) result = false;
        if(!validCategory()) result = false;
        if(!validSubcategory()) result = false;
        if(!validProducent()) result = false;

        return result;
    }

    private boolean validName() {

        if(this.name.length() == 0) {
            errors.add("Pole nazwa nie może być puste.");
            return false;
        }

        if(this.name.length() < 3 || this.name.length() > 100) {
            errors.add("Pole nazwa musi mieć od 3 do 100 znaków.");
            return false;
        }

        return true;
    }

    private boolean validDescription() {

        if(this.description.length() == 0) {
            errors.add("Pole opis nie może być puste.");
            return false;
        }

        if(this.description.length() < 10 || this.name.length() > 10000) {
            errors.add("Pole opis musi mieć od 10 do 10000 znaków.");
            return false;
        }

        return true;
    }

    private boolean validQuantity() {

        if(this.quantity.length() == 0) {
            errors.add("Pole ilość nie może być puste.");
            return false;
        }

        try {
            parsedQuantity = Integer.parseInt(this.quantity);
        } catch (NumberFormatException e) {
            errors.add("Pole ilość musi być liczbą.");
            return false;
        }

        return true;
    }

    private boolean validPrice() {

        if(this.price.length() == 0) {
            errors.add("Pole cena nie może być puste.");
            return false;
        }

        try {
            parsedPrice = Double.parseDouble(this.price);
        } catch (NumberFormatException e) {
            errors.add("Pole cena musi być liczbą.");
            return false;
        }

        return true;
    }

    private boolean validCategory() {
        if (!(this.category == null)) {
            for (Category category : listCategory) {
                if(category.getId() == this.category.getId()) return true;
            }
        }
        errors.add("Nie ma takiej kategorii");

        return false;
    }

    private boolean validSubcategory() {
        if (!(this.subcategory == null)) {
            for (Subcategory subcategory : listSubcategory) {
                if (subcategory.getId() == this.subcategory.getId()) return true;
            }
        }
        errors.add("Nie ma takiej podkategorii");

        return false;
    }

    private boolean validProducent() {
        if (!(this.producent == null)) {
            for (Producent producent : listProducent) {
                if (producent.getId() == this.producent.getId()) return true;
            }
        }
        errors.add("Nie ma takiego producenta");

        return false;
    }
}
