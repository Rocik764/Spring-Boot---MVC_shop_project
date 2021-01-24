package local.umg.susersmvc.validate;

import java.util.ArrayList;
import java.util.List;

public class ProductValidation {

    private String name;
    private String description;
    private String quantity;
    private String price;
    private List<String> errors = new ArrayList<>();
    private int parsedQuantity;
    private double parsedPrice;

    public ProductValidation(String name, String description, String quantity, String price) {
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
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
}
