package com.routee.weather.connection;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.springframework.stereotype.Component;

@Component
public class ConnectionHelper {

	public static final String API_KEY = "b385aa7d4e568152288b3c9f5c2458a5";
	public static final String CITY = "Thessaloniki";
	
	public String getWeather()
	{
		String endPoint = "http://api.openweathermap.org/data/2.5/weather?q=" + CITY + "&appid=" + API_KEY + "&units=metric";
		StringBuilder result = new StringBuilder();
		try 
		{
			URL url = new URL(endPoint);
			URLConnection urlConnection = url.openConnection();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			String line;
			while ((line = bufferedReader.readLine()) != null)
			{
				result.append(line);
			}
			
			bufferedReader.close();
		}
		catch (Exception e)  
		{
			e.printStackTrace();
		}
		
		return result.toString();
	}
}
