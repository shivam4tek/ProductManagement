package com.shivam.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.shivam.dao.entity.Product;
import com.shivam.dao.entity.ProductHit;
import com.shivam.dao.repository.IProductHitRepository;
import com.shivam.dao.repository.IProductRepository;
import com.shivam.helpers.Currency;
import com.shivam.helpers.CurrencyExchangeUtility;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProductService implements IProductService{
	
	@Autowired
	IProductRepository repository;
	
	@Autowired
	IProductHitRepository productHitRepo;
	
	@Autowired
	CacheService cacheService;
	
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public Product getProductById(Long id, Currency currency) {
		
		Product currentProduct = this.getSingleProduct(id);
		if (currentProduct == null) {
			return null;
		}
		
		Optional<ProductHit> hit = productHitRepo.findById(id);
		
		if(hit.isPresent()) {
			productHitRepo.save(ProductHit.builder().id(id).hit((hit.get().getHit()+1)).build());
		} else {
			productHitRepo.save(ProductHit.builder().id(id).hit(1).build());
		}
		
		if (currency != null && currency != Currency.USD) {
			currentProduct.setPrice(
					CurrencyExchangeUtility.getUpdatedPrice(
							cacheService.getCurrencyRatesFromCache(), 
							currentProduct.getPrice(), 
							currency.toString()
					)
			);
			currentProduct.setCurrency(currency.toString());
		}
		return currentProduct;
	}
	
	private Product getSingleProduct(Long id) {
		
		Product productFromCache = cacheService.getProductFromCache(id);
		if (null != productFromCache) {
			log.info("Product From Cache :: " + productFromCache);
			return Product.builder()
					.id(productFromCache.getId())
					.name(productFromCache.getName())
					.description(productFromCache.getDescription())
					.active(productFromCache.isActive())
					.currency(productFromCache.getCurrency())
					.price(productFromCache.getPrice())
					.build();
		}
		
		Optional<Product> product = repository.findById(id);
		if (!product.isPresent()) {
			return null;
		}
		
		Product productFromDatabase = product.get();
		
		log.info("Product From Database :: " + productFromDatabase);
		
		cacheService.putProductInCache(id, productFromDatabase);
		return Product.builder()
				.id(productFromDatabase.getId())
				.name(productFromDatabase.getName())
				.description(productFromDatabase.getDescription())
				.active(productFromDatabase.isActive())
				.currency(productFromDatabase.getCurrency())
				.price(productFromDatabase.getPrice())
				.build();
	}

	@Override
	public Product createProduct(Product product) {
		Product savedProduct = repository.save(product);
		return savedProduct;	
	}

	@Override
	public List<Product> getProductWithMostHits(Integer limit, Currency currency) {
		String query = "select * from product, product_hit where product.id = product_hit.id and active = true order by hit desc limit " + (limit != null ? limit : 5);
		List<Product> products = jdbcTemplate.query(query , new ProductRowMapper());
		if (currency != null && currency != Currency.USD) {
			return CurrencyExchangeUtility.getUpdatedPricesForList(
				cacheService.getCurrencyRatesFromCache(), 
				products, 
				currency.toString()
			);
		}
		return products.stream()
				.filter(item -> item.isActive() == true)
				.collect(Collectors.toList());
	}

	@Override
	public void deleteProduct(Long id, Product existingProduct) {
		existingProduct.setActive(false);
		repository.save(existingProduct);
		cacheService.putProductInCache(id, existingProduct);
	}	
}

class ProductRowMapper implements RowMapper<Product> {
	@Override
	public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
		Product product = Product.builder()
				.id(rs.getLong("id"))
				.name(rs.getString("name"))
				.price(rs.getDouble("price"))
				.description(rs.getString("description"))
				.active(rs.getBoolean("active"))
				.build();
		return product;
	}

}
