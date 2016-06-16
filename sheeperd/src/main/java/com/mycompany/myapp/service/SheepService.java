package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Herd;
import com.mycompany.myapp.domain.Order;
import com.mycompany.myapp.domain.Sheep;
import com.mycompany.myapp.domain.Stock;
import com.mycompany.myapp.repository.SheepRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing Sheep.
 */
@Service
@Transactional
public class SheepService {

    private final Logger log = LoggerFactory.getLogger(SheepService.class);
    
    @Inject
    private SheepRepository sheepRepository;
    
    /**
     * Save a sheep.
     * @return the persisted entity
     */
    public Sheep save(Sheep sheep) {
        log.debug("Request to save Sheep : {}", sheep);
        Sheep result = sheepRepository.save(sheep);
        return result;
    }

    /**
     *  get all the sheeps.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Sheep> findAll(Pageable pageable) {
        log.debug("Request to get all Sheeps");
        Page<Sheep> result = sheepRepository.findAll(pageable); 
        return result;
    }
    
    
    /**
     *  get all the sheeps existing.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Stock calculateStock(int days) {
    	Stock stock = new Stock();
        log.debug("Request to get all Sheeps");
        List<Sheep> existingSheeps = sheepRepository.findAll();
        double sumSheepsMilk = 0;
        double sumSkins = 0;
        for(Sheep sheepSample : existingSheeps) {
        	double sheepDailyMilk = 0;
        	double sheepTotalMilk = 0;
        	double sheepShaved = 1;
        	int currentDay = 0;
        	while (currentDay < days && (sheepSample.getAge()+currentDay*0.01)<10){
                double sheepCurrentAge = sheepSample.getAge() + currentDay*0.01;
        		sheepDailyMilk = (50-(sheepSample.getAge()*100+currentDay)*0.03);
        		currentDay++;
        		sheepTotalMilk += sheepDailyMilk;

//                if(sheepSample.getAge_last_shaved()==null){
//                	sheepSample.setAge_last_shaved(1.0);
//                } else if((sheepCurrentAge - sheepSample.getAge_last_shaved()) > 8){
//                	sheepSample.setAge_last_shaved(sheepCurrentAge);
//                	sheepShaved++;
//                }
                log.debug("currentDay "+currentDay);
                log.debug("sheepSample.getAge() "+sheepSample.getAge());
                log.debug("currentDay%(8+sheepCurrentAge)==0.0 "+currentDay%(8+sheepSample.getAge()));

        		if(currentDay%(8+sheepSample.getAge())==0){

        			sheepShaved++;
        			log.debug("PLUS SHEEP SKIN "+sheepShaved);
        		}
        	}
        	sumSheepsMilk += sheepTotalMilk;
        	sumSkins += sheepShaved;
        }
        log.debug("Total amount of Milk: {}", sumSheepsMilk);
        stock.setMilk(sumSheepsMilk);
        stock.setSkins(sumSkins);
        return stock;
    }
    
    public Herd calculateHerd(int days) {
    	
    	double sheepCentury = 10.0;
        List<Sheep> existingSheeps = sheepRepository.findAllByAgeLessThan(sheepCentury);
        Herd herd = new Herd();
        herd.setHerd(existingSheeps);
        
		return herd;
	}
    
    
	public Stock buyStock(int days, Order order) {
		
		Stock currentStock = calculateStock(days);
		Stock boughtStock = new Stock();
		if(currentStock.getMilk()>=order.getStock().getMilk()){
			boughtStock.setMilk(order.getStock().getMilk());
		}
		System.out.println("CURRENT SKINS"+currentStock.getSkins());
		System.out.println("DEMANDED SKINS"+order.getStock().getSkins());

		if(currentStock.getSkins()>=order.getStock().getSkins()){
			boughtStock.setSkins(order.getStock().getSkins());
		}

		return boughtStock;
	}
	
    /**
     *  get one sheep by id.
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Sheep findOne(Long id) {
        log.debug("Request to get Sheep : {}", id);
        Sheep sheep = sheepRepository.findOne(id);
        return sheep;
    }

    /**
     *  delete the  sheep by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Sheep : {}", id);
        sheepRepository.delete(id);
    }

}
