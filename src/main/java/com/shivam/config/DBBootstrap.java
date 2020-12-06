package com.shivam.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import com.shivam.dao.entity.Product;
import com.shivam.dao.repository.IProductRepository;

@Configuration
public class DBBootstrap implements CommandLineRunner {
	
	@Autowired
	IProductRepository repository;

	@Override
	public void run(String... args) throws Exception {
		Product productObj1 = Product.builder()
				.name("Iphone12")
				.description("This is Iphone12. One of the most selling Iphone")
				.price(2142.99)
				.build();
		Product productObj2 = Product.builder()
				.name("Realme7")
				.description("Realme7 has the best camera.")
				.price(300.00)
				.build();
		repository.save(productObj1);
		repository.save(productObj2);
	}

}
