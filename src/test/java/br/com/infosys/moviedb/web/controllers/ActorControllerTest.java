package br.com.infosys.moviedb.web.controllers;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.infosys.moviedb.MovieDbApplication;
import br.com.infosys.moviedb.core.services.ActorService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MovieDbApplication.class)
@WebIntegrationTest
public class ActorControllerTest {
	
	private static final String URL_ACTOR = "http://localhost:8080/v1/actor/";

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	@Autowired
	private ActorService actorService;

	private RestTemplate restTemplate = new TestRestTemplate();

	@Test
	public void createActor() throws JsonProcessingException {
		//build request data
		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("name", "João");
		requestBody.put("biography", "This is a brief test biography.");
		requestBody.put("country", "Brazil");

		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);

		HttpEntity<String> httpEntity = new HttpEntity<String>(OBJECT_MAPPER.writeValueAsString(requestBody), requestHeaders);		
		
		Map<String, Object> response = restTemplate.postForObject(URL_ACTOR, httpEntity, Map.class, Collections.EMPTY_MAP);
		
		//assert the response
		Assert.assertNotNull(response);
		
		//assert that the object is valid
		response.get("actorId");
		//Assert.assertNotNull(object);
		
		//response.get(key)
	}

}
