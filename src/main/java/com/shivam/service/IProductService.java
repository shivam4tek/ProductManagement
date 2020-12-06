package com.shivam.service;

import java.util.List;

import com.shivam.dao.entity.Product;
import com.shivam.helpers.Currency;


public interface IProductService {
	public Product getProductById(Long id, Currency currency);
	public Product createProduct(Product product);
	public List<Product> getProductWithMostHits(Integer limit, Currency currency);
	public void deleteProduct(Long id, Product existingProduct);
}
