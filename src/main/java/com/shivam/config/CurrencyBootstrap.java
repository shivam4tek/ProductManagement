package com.shivam.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.shivam.service.CacheService;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableScheduling
@Slf4j
public class CurrencyBootstrap{
	
	@Autowired
	CacheService cacheService;
	
	@Scheduled(fixedRateString ="${scheduling.fetchApiInterval}")
	public void evictAllcachesAtIntervals() {
		log.info("evictAllcachesAtIntervals START");
		cacheService.evictAllCaches();
		cacheService.getCurrencyRatesFromCache();
		log.info("evictAllcachesAtIntervals END");
	}
}
