package com.shivam.helpers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.shivam.dao.entity.Product;

public class CurrencyExchangeUtility {
	
	static Gson gson = new Gson();
	
	public static String fetchCurrencyData() {
		RestTemplate restTemplate = new RestTemplate();
		String fooResourceUrl
		  = "https://open.exchangerate-api.com/v6/latest";
		ResponseEntity<String> response
		  = restTemplate.getForEntity(fooResourceUrl, String.class);
		JsonObject result = gson.fromJson( response.getBody(), JsonObject.class);
		JsonObject rates = gson.fromJson( result.get("rates").toString() , JsonObject.class);
		return rates.toString();
		
	}
	
	public static Double getUpdatedPrice(String rates, Double price, String currency) {
		JsonObject jsonObject = gson.fromJson(rates, JsonObject.class);
		Double rate = jsonObject.get(currency).getAsDouble();
		BigDecimal bd = new BigDecimal(rate*price).setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
	}
	
	public static List<Product> getUpdatedPricesForList(String rates, List<Product> products, String currency) {
		JsonObject jsonObject = gson.fromJson(rates, JsonObject.class);
		Double rate = jsonObject.get(currency).getAsDouble();
		
		return products.stream()
		.map(item -> {
			BigDecimal bd = new BigDecimal(rate*item.getPrice()).setScale(2, RoundingMode.HALF_UP);
			item.setPrice(bd.doubleValue());
			item.setCurrency(currency);
			return item;
		}).collect(Collectors.toList());
	}
}
