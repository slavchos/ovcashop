package com.mycompany.myapp.domain;


import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Stock.
 */
@Entity
@Table(name = "stock")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Stock implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "day")
    private int day;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "milk")
    private Double milk;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "skins")
    private Double skins;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public Double getMilk() {
        return milk;
    }

    public void setMilk(Double milk) {
        this.milk = milk;
    }

    public Double getSkins() {
    	return skins;
    }
    
    public void setSkins(Double skins) {
    	this.skins = skins;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Stock sheep = (Stock) o;
        return Objects.equals(id, sheep.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "{" +
            " milk='" + milk + "'" +
            ", skins='" + skins + "'" +
            '}';
    }
}
