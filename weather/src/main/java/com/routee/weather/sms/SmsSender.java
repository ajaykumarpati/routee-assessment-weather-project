package com.routee.weather.sms;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.Base64.Encoder;

import org.json.simple.JSONObject;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class SmsSender {

	public static final String APPLICATION_ID = "5c5d5e28e4b0bae5f4accfec";
	public static final String APPLICATION_SECRET = "MGkNfqGud0";
	ObjectMapper objectMapper = new ObjectMapper();
	
	/**
	 * Generates the access token for sending the SMS
	 * 
	 * @return access token
	 * @throws Exception
	 */
	public String getAccessToken() throws Exception
	{
		Encoder encoder = Base64.getEncoder();
		String encodedHeader = encoder.encodeToString((APPLICATION_ID + ":" + APPLICATION_SECRET).getBytes());
		URL url = new URL("https://auth.routee.net/oauth/token");
		String line;
		StringBuilder stringBuilder = new StringBuilder();
		
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod(HttpMethod.POST.toString());
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setRequestProperty("authorization", "Basic " + encodedHeader);
		connection.setRequestProperty("content-type", "application/x-www-form-urlencoded");
		
		OutputStream outputStream = connection.getOutputStream();
		outputStream.write("grant_type=client_credentials".getBytes());
		outputStream.flush();
		
		connection.connect();
		
		if(connection.getResponseCode() == 200)
		{
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			while((line = bufferedReader.readLine()) != null)
			{
				stringBuilder.append(line);
				stringBuilder.append(System.lineSeparator());
			}
		}
		
		JsonNode tokenResponse = objectMapper.readValue(stringBuilder.toString(), JsonNode.class);
		
		return tokenResponse.get("access_token").asText();
	}
	
	/**
	 * Sends the SMS via the Routee API
	 * 
	 * @param token
	 * @param smsText
	 * @param smsNumber
	 * @throws Exception
	 */
	public void sendSms(String token, String smsText, String smsNumber) throws Exception
	{
		URL url = new URL("https://connect.routee.net/sms");
		String line;
		StringBuilder stringBuilder = new StringBuilder();
		
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod(HttpMethod.POST.toString());
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setRequestProperty("authorization", "Bearer " + token);
		connection.setRequestProperty("content-type", "application/json");
		
		String smsPayload = generateSmspayload(smsText, smsNumber);
		
		OutputStream outputStream = connection.getOutputStream();
		outputStream.write(smsPayload.getBytes());
		outputStream.flush();
		
		connection.connect();
		
		if(connection.getResponseCode() == 200)
		{
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			while((line = bufferedReader.readLine()) != null)
			{
				stringBuilder.append(line);
				stringBuilder.append(System.lineSeparator());
			}
		}
		
	}
	
	/**
	 * Generates the payload for the SMS sending through Routee API
	 * 
	 * @param smsText
	 * @param smsNumber
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String generateSmspayload(String smsText, String smsNumber)
	{
		JSONObject payload = new JSONObject();
		payload.put("body", smsText);
		payload.put("to", smsNumber);
		payload.put("from", "amdTelecom");
		return payload.toJSONString();
		
	}
}
