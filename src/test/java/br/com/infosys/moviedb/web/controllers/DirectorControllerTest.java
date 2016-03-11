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
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.infosys.moviedb.MovieDbApplication;
import br.com.infosys.moviedb.core.services.DirectorService;
import br.com.infosys.moviedb.domain.entities.Director;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MovieDbApplication.class)
@WebIntegrationTest
public class DirectorControllerTest {

	private static final String URL_DIRECTOR = "http://localhost:8080/v1/director";

	@Autowired
	private DirectorService directorService;

	private RestTemplate restTemplate = new TestRestTemplate();
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	@Test
	public void createDirector() throws JsonProcessingException {
		// build request data
		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("name", "Vitor");
		requestBody.put("biography", "This is a brief test biography.");
		requestBody.put("country", "Japan");

		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);

		HttpEntity<String> httpEntity = new HttpEntity<String>(OBJECT_MAPPER.writeValueAsString(requestBody),
				requestHeaders);

		Map<String, Object> response = restTemplate.postForObject(URL_DIRECTOR, httpEntity, Map.class,
				Collections.EMPTY_MAP);

		// assert the response
		Assert.assertNotNull(response);

		// assert that the object is valid
		Long directorId = Long.valueOf(response.get("idDirector").toString());

		Assert.assertNotNull(directorId);

		Director directorFromDb = directorService.findById(directorId);
		Assert.assertEquals("Vitor", directorFromDb.getName());
		Assert.assertEquals("This is a brief test biography.", directorFromDb.getBiography());
		Assert.assertEquals("Japan", directorFromDb.getCountry());

		// remove object from DB.
		directorService.delete(directorFromDb);
	}

}
