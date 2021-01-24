package local.umg.susersmvc.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "producent", schema = "zoologiczny_spring")
public class Producent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank(message = "Pole nazwa podkategorii nie może być puste")
    @Size(min=3, max=100)
    private String name;

    @NotNull
    @NotBlank(message = "Pole charakterystyka nie może być puste")
    @Size(min=20, max=10000)
    private String characteristics;

    @NotBlank(message = "Pole telefon nie może być puste")
    @Pattern(regexp = "\\+\\d{2}\\s\\d{3}\\s\\d{3}\\s\\d{3}")
    private String phone;

    @OneToMany
    @JoinColumn(name = "id")
    private List<Product> products;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCharacteristics() {
        return characteristics;
    }

    public void setCharacteristics(String characteristics) {
        this.characteristics = characteristics;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
