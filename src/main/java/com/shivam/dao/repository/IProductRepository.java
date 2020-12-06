package com.shivam.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shivam.dao.entity.Product;

@Repository
public interface IProductRepository extends JpaRepository<Product, Long>{

}
