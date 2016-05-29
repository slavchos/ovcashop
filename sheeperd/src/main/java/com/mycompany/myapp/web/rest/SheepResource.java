package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Herd;
import com.mycompany.myapp.domain.Order;
import com.mycompany.myapp.domain.Sheep;
import com.mycompany.myapp.domain.Stock;
import com.mycompany.myapp.service.SheepService;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import com.mycompany.myapp.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * REST controller for managing Sheep.
 */
@RestController
@RequestMapping("/api")
public class SheepResource {

	private final Logger log = LoggerFactory.getLogger(SheepResource.class);

	@Inject
	private SheepService sheepService;

	/**
	 * POST /sheeps -> Create a new sheep.
	 */
	@RequestMapping(value = "/sheeps", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Sheep> createSheep(@RequestBody Sheep sheep) throws URISyntaxException {
		log.debug("REST request to save Sheep : {}", sheep);
		if (sheep.getId() != null) {
			return ResponseEntity.badRequest()
					.headers(
							HeaderUtil.createFailureAlert("sheep", "idexists", "A new sheep cannot already have an ID"))
					.body(null);
		}
		Sheep result = sheepService.save(sheep);
		return ResponseEntity.created(new URI("/api/sheeps/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert("sheep", result.getId().toString())).body(result);
	}

	/**
	 * PUT /sheeps -> Updates an existing sheep.
	 */
	@RequestMapping(value = "/sheeps", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Sheep> updateSheep(@RequestBody Sheep sheep) throws URISyntaxException {
		log.debug("REST request to update Sheep : {}", sheep);
		if (sheep.getId() == null) {
			return createSheep(sheep);
		}
		Sheep result = sheepService.save(sheep);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("sheep", sheep.getId().toString()))
				.body(result);
	}

	/**
	 * GET /sheeps -> get all the sheeps.
	 */
	@RequestMapping(value = "/sheeps", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<List<Sheep>> getAllSheeps(Pageable pageable) throws URISyntaxException {
		log.debug("REST request to get a page of Sheeps");
		Page<Sheep> page = sheepService.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sheeps");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /sheeps/:id -> get the "id" sheep.
	 */
	@RequestMapping(value = "/sheeps/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Sheep> getSheep(@PathVariable Long id) {
		log.debug("REST request to get Sheep : {}", id);
		Sheep sheep = sheepService.findOne(id);
		return Optional.ofNullable(sheep).map(result -> new ResponseEntity<>(result, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	/**
	 * GET /sheep-shop/stock/{days} -> read XML file.
	 * 
	 * @return
	 * @throws URISyntaxException 
	 */
	@RequestMapping(value = "/sheeps/readfile", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public Stock readFile() throws URISyntaxException {
		log.debug("REST request READ FILE : {}");

		SAXBuilder builder = new SAXBuilder();
		File xmlFile = new File("src/main/resources/herd.xml");

		try {

			Document document = (Document) builder.build(xmlFile);
			Element rootNode = document.getRootElement();
			List<?> list = rootNode.getChildren("mountainsheep");

			for (int i = 0; i < list.size(); i++) {

				Element node = (Element) list.get(i);

				Sheep sheepSample = new Sheep();
				sheepSample.setName(node.getAttributeValue("name"));
				System.out.println("First Name : " + node.getAttributeValue("name"));
				
				sheepSample.setAge(Double.parseDouble(node.getAttributeValue("age")));
				System.out.println("Age : " + node.getAttributeValue("age"));
				
//				sheepSample.setName(node.getAttributeValue("sex"));
//				System.out.println("Sex : " + node.getAttributeValue("sex"));

				updateSheep(sheepSample);
			}

		} catch (IOException io) {
			System.out.println(io.getMessage());
		} catch (JDOMException jdomex) {
			System.out.println(jdomex.getMessage());
		}

		return null;
	}

	/**
	 * GET /sheep-shop/stock/{days} -> get the "days" sheep.
	 * 
	 * @return
	 */
	@RequestMapping(value = "/sheep-shop/stock/{days}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public Stock getStock(@PathVariable("days") int days) {
		log.debug("REST request to get STOCK MILK : {}", days);
		Stock sumSheepsMilk = sheepService.calculateStock(days);

		return sumSheepsMilk;
	}

	/**
	 * GET /sheep-shop/herd/{days} -> get the "herd" sheep.
	 * 
	 * @return
	 */
	@RequestMapping(value = "/sheep-shop/herd/{days}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public Herd getHerd(@PathVariable("days") int days) {
		log.debug("REST request to get Herd : {}", days);
		Herd sheepsHerd = sheepService.calculateHerd(days);

		return sheepsHerd;
	}

	/**
	 * POST /sheep-shop/order/{days} -> order
	 * 
	 * @return
	 */
	@RequestMapping(value = "/sheep-shop/order/{days}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<?> getOrder(@PathVariable String days, @Valid @RequestBody Order order)
			throws URISyntaxException {
		log.debug("REST getOrder : DAYS {}", days);

		log.debug("REST getOrder : ORDER {}", order);

		log.debug("REST getOrder : DAY", order.getStock().getDay());

		Stock boughtStock = sheepService.buyStock(order.getStock().getDay(), order);

		HttpStatus returnStatus;
		if(boughtStock.getMilk()!= null && boughtStock.getSkins()!=null) {
			returnStatus = HttpStatus.OK;
		}else
			returnStatus = HttpStatus.PARTIAL_CONTENT;

		return Optional.ofNullable(boughtStock).map(result -> new ResponseEntity<>(result, returnStatus))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	/**
	 * DELETE /sheeps/:id -> delete the "id" sheep.
	 */
	@RequestMapping(value = "/sheeps/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Void> deleteSheep(@PathVariable Long id) {
		log.debug("REST request to delete Sheep : {}", id);
		sheepService.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("sheep", id.toString())).build();
	}
}
