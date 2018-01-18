package com.cab.allocation.registration.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestUtils {

	private RestTemplate restTemplate;

	@Autowired
	public RestUtils(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public Boolean isDropPointPresent(String dropPoint) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Object> entity = new HttpEntity<Object>(headers);

		
		ResponseEntity<Void> out = restTemplate
				.exchange("/dropPoints?name=" + dropPoint, HttpMethod.HEAD, entity,
				Void.class);
		
		if (out.getStatusCode() == HttpStatus.OK)
			return true;
		return false;

	}

}
