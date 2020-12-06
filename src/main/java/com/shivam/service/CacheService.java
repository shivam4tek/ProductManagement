package com.shivam.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import com.shivam.dao.entity.Product;
import com.shivam.helpers.CurrencyExchangeUtility;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CacheService {

	@Autowired
	CacheManager cacheManager;
	
	public String getCurrencyRatesFromCache() {
		Cache cache = cacheManager.getCache("exchangeRates");
		if (cache.get("rates") != null) {
			log.info("Cache HIT");
			return cache.get("rates").get().toString();
		}
		log.info("Cache MISS");
		return fetchCurrencyRates();
	}
	
	public String fetchCurrencyRates() {
		String rates = CurrencyExchangeUtility.fetchCurrencyData();
		putRatesInCache(rates);
		return rates;
	}
	
	public void putRatesInCache(String rates) {
		Cache cache = cacheManager.getCache("exchangeRates");
		cache.put("rates", rates);
	}
	
	public void evictAllCaches() {
	    cacheManager.getCacheNames().stream()
	      .forEach(cacheName -> cacheManager.getCache(cacheName).clear());
	}
	
	public void putProductInCache(Long id, Product product) {
		Cache cache = cacheManager.getCache("products");
		cache.put(id, product);
	}
	
	public Product getProductFromCache(Long id) {
		Cache cache = cacheManager.getCache("products");
		ValueWrapper valueWrapper = cache.get(id);
		if (valueWrapper != null && valueWrapper.get() != null) {
			return (Product)valueWrapper.get();
		}
		return null;
	}
}
