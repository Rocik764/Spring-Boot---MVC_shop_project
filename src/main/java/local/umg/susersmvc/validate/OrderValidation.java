package local.umg.susersmvc.validate;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OrderValidation {

    private String delivery;
    private String payment;
    private String address;
    private String code;
    private String city;
    private String phone;
    private String regulations;
    private List<String> errors = new ArrayList<>();
    private String[] deliveryOptions = {"kurier", "osobiście", "paczkomat"};
    private String[] paymentOptions = {"blik", "przelew", "przy odbiorze", "karta płatnicza", "dotpay"};

    public OrderValidation(String delivery, String payment, String address, String code, String city, String phone, String regulations) {
        this.delivery = delivery;
        this.payment = payment;
        this.address = address;
        this.code = code;
        this.city = city;
        this.phone = phone;
        this.regulations = regulations;
    }

    public List<String> getErrors() {
        return this.errors;
    }

    public boolean validate() {

        boolean result = true;

        if(!validDelivery()) result = false;
        if(!validPayment()) result = false;
        if(!validAddress()) result = false;
        if(!validCode()) result = false;
        if(!validCity()) result = false;
        if(!validPhone()) result = false;
        if(!validRegulations()) result = false;

        return result;
    }

    private boolean validDelivery() {

        if(this.delivery.length() == 0) {
            errors.add("Pole dostawa musi być zaznaczone.");
            return false;
        }

        for (String item : deliveryOptions) {
            if(item.equals(this.delivery)) {
                return true;
            }
        }
        errors.add("Pole dostawa musi być jedną z dostępnych opcji");

        return false;
    }

    private boolean validPayment() {

        if(this.payment.length() == 0) {
            errors.add("Pole płatność musi być zaznaczone.");
            return false;
        }

        for (String item : paymentOptions) {
            if(item.equals(this.payment)) {
                return true;
            }
        }
        errors.add("Pole płatność musi być jedną z dostępnych opcji");

        return false;
    }

    private boolean validAddress() {

        if(this.address.length() == 0) {
            errors.add("Pole adres nie może być puste.");
            return false;
        }

        if(this.address.length() < 5 || this.address.length() > 100) {
            errors.add("Pole adres musi mieć od 5 do 100 znaków.");
            return false;
        }

        return true;
    }

    private boolean validCode() {

        if(this.code.length() == 0) {
            errors.add("Pole kodu pocztowego nie może być puste.");
            return false;
        }

        Pattern pattern = Pattern.compile("([0-9][0-9]\\-[0-9][0-9][0-9])");
        Matcher matcher = pattern.matcher(this.code);
        if(!matcher.matches()) {
            errors.add("Pole kodu pocztowego musi mieć poprawny format, np. 11-111");
            return false;
        }

        return true;
    }

    private boolean validCity() {

        if(this.code.length() == 0) {
            errors.add("Pole miasto nie może być puste.");
            return false;
        }

        Pattern pattern = Pattern.compile("^[A-Z-ŻŹĆĄŚĘŁÓŃ][a-zA-Z-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]+$");
        Matcher matcher = pattern.matcher(this.city);
        if(!matcher.matches()) {
            errors.add("Pole miasto musi mieć poprawny format, np. Gdańsk");
            return false;
        }

        return true;
    }

    private boolean validPhone() {

        if(this.code.length() == 0) {
            errors.add("Pole telefon nie może być puste.");
            return false;
        }

        Pattern pattern = Pattern.compile("\\+\\d{2}\\s\\d{3}\\s\\d{3}\\s\\d{3}");
        Matcher matcher = pattern.matcher(this.phone);
        if(!matcher.matches()) {
            errors.add("Pole telefon musi mieć poprawny format, np. +48 123 123 123");
            return false;
        }

        return true;
    }

    private boolean validRegulations() {

        if(this.regulations == null) {
            errors.add("Pole regulaminu musi być zaznaczone.");
            return false;
        }

        return true;
    }
}
