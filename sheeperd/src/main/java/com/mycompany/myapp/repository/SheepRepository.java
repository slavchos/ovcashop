package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Sheep;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Sheep entity.
 */
public interface SheepRepository extends JpaRepository<Sheep,Long> {
	
	List<Sheep> findAllByAgeLessThan(double age);

}
