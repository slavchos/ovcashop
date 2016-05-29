package com.mycompany.myapp.domain;


import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import com.mycompany.myapp.domain.Sheep;
/**
 * A Stock.
 */
@Entity
@Table(name = "herd")
public class Herd implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonIgnore
    @Column(name = "day")
    private Double day;

    @OneToMany(targetEntity=Sheep.class, fetch=FetchType.EAGER)
    private List<Sheep> herdSheep;
    
    @JsonIgnore
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getDay() {
        return day;
    }

    public void setDay(Double day) {
        this.day = day;
    }

    public List<Sheep> getHerd() {
    	return herdSheep;
    }
    
    public void setHerd(List<Sheep> herdSheep) {
    	this.herdSheep = herdSheep;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Herd sheep = (Herd) o;
        return Objects.equals(id, sheep.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "{" +
            ", herd='" + herdSheep + "'" +
            '}';
    }
}
