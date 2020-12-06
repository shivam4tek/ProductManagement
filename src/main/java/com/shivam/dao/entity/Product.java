package com.shivam.dao.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shivam.helpers.Currency;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	String name;
	Double price;
	String description;
	
	// making it @Builder.Default because Lombok builder does not take default value.
	@Builder.Default
	@JsonIgnore
	boolean active = true;
	
	@Builder.Default
	String currency = Currency.USD.toString();
}
