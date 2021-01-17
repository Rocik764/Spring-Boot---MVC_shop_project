package local.umg.susersmvc.model;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "orders_details", schema = "zoologiczny_spring")
public class OrderDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private int amount;
    private Date purchase;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Date getPurchase() {
        return purchase;
    }

    public void setPurchase(Date purchase) {
        this.purchase = purchase;
    }

    @Override
    public String toString() {
        return "ID: " + id
                + ", Produkt: " + product.getName()
                + ", Użytkownik: " + user.getEmail()
                + ", Ilość: " + amount + "  ";
    }
}
