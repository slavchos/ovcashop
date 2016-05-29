package com.mycompany.myapp.domain;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Order.
 */
@Entity
public class Order  implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
    @Column(name = "customer")
    private String customer;

    @Column(name = "stock")
    private Stock stock;
    
    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }
    
    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Order sheep = (Order) o;
        if (!Objects.equals(stock.getId(), sheep.stock.getId()))
            return false;

        return true;
    }

   
    @Override
    public int hashCode() {
        return Objects.hashCode(stock.getId());
    }

    @Override
    public String toString() {
        return "{" +
        		  "customer='" + customer + "'" +
        		  ", order='" + stock + "'" +
            '}';
    }
}
