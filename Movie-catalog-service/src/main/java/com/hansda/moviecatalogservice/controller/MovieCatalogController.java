package com.hansda.moviecatalogservice.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.hansda.moviecatalogservice.model.CatalogItem;
import com.hansda.moviecatalogservice.model.Movie;
import com.hansda.moviecatalogservice.model.Rating;
import com.hansda.moviecatalogservice.model.UserRating;



@RestController
@RequestMapping("/catalog")
public class MovieCatalogController {

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private CircuitBreakerFactory circuitBreakerFactory;
	
	@RequestMapping("/{userId}")
	public List<CatalogItem> getCatalog(@PathVariable("userId") String userId){
		
		CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
		
		UserRating ratings = circuitBreaker.run(() -> restTemplate.getForObject("http://ratings-data-service/ratingsdata/user/" + userId,
				UserRating.class), throwable -> getFallbackRating());
		
		
		return ratings.getUserRatings().stream().map(rating -> {
			Movie movie = circuitBreaker.run(()-> restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(),
					Movie.class), throwable -> getFallbackMovieInfo());
			
			return new CatalogItem(movie.getName(), movie.getDescription(), rating.getRating());
		}).collect(Collectors.toList());
		
	}
	
	private UserRating getFallbackRating() {
		return new UserRating(Arrays.asList(new Rating("No id", 0)));
	}

	private Movie getFallbackMovieInfo() {
		return new Movie("No id", "No name", "");
	}

	public List<CatalogItem> getCatalogFallbackMethod(@PathVariable("userId") String userId){
		return Arrays.asList(new CatalogItem("No movie name", "", 0));
	}
}
