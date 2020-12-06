package com.shivam.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.shivam.dao.entity.Product;
import com.shivam.helpers.Currency;
import com.shivam.service.CacheService;
import com.shivam.service.IProductService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(path = "/product")
@Slf4j
public class ProductController {

	@Autowired
	CacheService cacheService;

	@Autowired
	IProductService productService;

	@PostMapping
	public ResponseEntity<HttpStatus> createProduct(@RequestBody Product product) {
		Product model = productService.createProduct(product);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(model.getId())
				.toUri();
		log.info("Employee " + model + " created. Reference URI:: " + uri);
		return ResponseEntity.created(uri).build();
	}

	@GetMapping(path = "/{id}")
	public ResponseEntity<Product> getSingleProduct(@PathVariable Long id, @RequestParam(required = false) Currency currency) {
		Product model = productService.getProductById(id, currency);
		if (null == model) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}		
		return new ResponseEntity<>(model, HttpStatus.OK);
	}
	
	@GetMapping(path = "/mostViewed")
	public ResponseEntity<List<Product>> getMostViewProduct(@RequestParam(required = false) Currency currency, @RequestParam(required = false) Integer limit) {
		List<Product> model = productService.getProductWithMostHits(limit, currency);
		if (null == model) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(model, HttpStatus.OK);
	}
	
	@DeleteMapping(path = "/{id}")
	private ResponseEntity<HttpStatus> deleteProduct(@PathVariable Long id) {
		Product existingProduct = productService.getProductById(id, Currency.USD);
		if (null == existingProduct) {
			log.info("Product with id " + id + " does not exists");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		productService.deleteProduct(id, existingProduct);
		log.info("Deleted Employee:: " + existingProduct);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	} 

}
