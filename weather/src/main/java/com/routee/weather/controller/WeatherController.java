package com.routee.weather.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.routee.weather.service.WeatherService;

@RestController
@RequestMapping("/api")
public class WeatherController {

	@Autowired
	WeatherService weatherService;
	
	@PostMapping("/examineWeather")
	public String examineWeather() throws Exception
	{
		return "The temperature of Thessaloniki : " + weatherService.examineWeather();
	}
}
