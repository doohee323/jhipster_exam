package net.slipp.jhipster.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A CartItems.
 */
@Entity
@Table(name = "cart_items")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "cartitems")
public class CartItems implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "create_dt")
    private ZonedDateTime createDt;

    @ManyToOne
    private Product product;

    @ManyToOne
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getCreateDt() {
        return createDt;
    }

    public CartItems createDt(ZonedDateTime createDt) {
        this.createDt = createDt;
        return this;
    }

    public void setCreateDt(ZonedDateTime createDt) {
        this.createDt = createDt;
    }

    public Product getProduct() {
        return product;
    }

    public CartItems product(Product product) {
        this.product = product;
        return this;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public User getUser() {
        return user;
    }

    public CartItems user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CartItems cartItems = (CartItems) o;
        if (cartItems.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cartItems.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CartItems{" +
            "id=" + getId() +
            ", createDt='" + getCreateDt() + "'" +
            "}";
    }
}
