package com.mycompany.myapp.domain;


import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Sheep.
 */
@Entity
@Table(name = "sheep")
public class Sheep implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "age")
    private Double age;

    @Column(name = "age_last_shaved")
    private Double age_last_shaved;

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

    public Double getAge() {
        return age;
    }

    public void setAge(Double age) {
        this.age = age;
    }

    public Double getAge_last_shaved() {
        return age_last_shaved;
    }

    public void setAge_last_shaved(Double age_last_shaved) {
        this.age_last_shaved = age_last_shaved;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Sheep sheep = (Sheep) o;
        return Objects.equals(id, sheep.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Sheep{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", age='" + age + "'" +
            ", age_last_shaved='" + age_last_shaved + "'" +
            '}';
    }
}
