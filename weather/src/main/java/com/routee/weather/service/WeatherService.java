package com.routee.weather.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.routee.weather.connection.ConnectionHelper;
import com.routee.weather.sms.SmsSender;

@Service
public class WeatherService {

	@Autowired
	ConnectionHelper connectionHelper;

	@Autowired
	SmsSender smsSender;

	ObjectMapper objectMapper = new ObjectMapper();

	public String examineWeather() throws Exception {
		JsonNode main = null;

		JsonNode weatherResponse = objectMapper.readValue(connectionHelper.getWeather(), JsonNode.class);
		main = weatherResponse.get("main");
		double temperature = main.get("temp").asDouble();

		sendTemperatureMessage(temperature);

		return String.valueOf(temperature);
	}

	private void sendTemperatureMessage(double temperature) throws Exception 
	{
		String smsText;
		String smsNumber;
		if(temperature > 20)
		{
			smsText = "Your name and Temperature more than 20C. Temperature=" + temperature;
			smsNumber = "+30 6922222222";
		}
		else
		{
			smsText = "Your name and Temperature less than 20C. Temperature=" + temperature;
			smsNumber = "+30 6922222222";
		}

		String accessToken = smsSender.getAccessToken();


		int waitCouont = 0;
		while(waitCouont < 10)
		{
			smsSender.sendSms(accessToken, smsText, smsNumber);
			Thread.sleep( 10 * 60 * 1000);
			waitCouont++;
		}
	}


}
