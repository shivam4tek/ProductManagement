package com.shivam.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shivam.dao.entity.ProductHit;

public interface IProductHitRepository extends JpaRepository<ProductHit, Long>{

}
