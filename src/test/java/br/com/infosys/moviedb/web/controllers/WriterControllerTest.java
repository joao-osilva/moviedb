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
import br.com.infosys.moviedb.core.services.WriterService;
import br.com.infosys.moviedb.domain.entities.Writer;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MovieDbApplication.class)
@WebIntegrationTest
public class WriterControllerTest {

	private static final String URL_WRITER = "http://localhost:8080/v1/writer";

	@Autowired
	private WriterService writerService;

	private RestTemplate restTemplate = new TestRestTemplate();
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	@Test
	public void createWriter() throws JsonProcessingException {
		// build request data
		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("name", "Marco");
		requestBody.put("biography", "This is a brief test biography.");
		requestBody.put("country", "Spain");

		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);

		HttpEntity<String> httpEntity = new HttpEntity<String>(OBJECT_MAPPER.writeValueAsString(requestBody),
				requestHeaders);

		Map<String, Object> response = restTemplate.postForObject(URL_WRITER, httpEntity, Map.class,
				Collections.EMPTY_MAP);

		// assert the response
		Assert.assertNotNull(response);

		// assert that the object is valid
		Long writerId = Long.valueOf(response.get("idWriter").toString());

		Assert.assertNotNull(writerId);

		Writer writerFromDb = writerService.findById(writerId);
		Assert.assertEquals("Marco", writerFromDb.getName());
		Assert.assertEquals("This is a brief test biography.", writerFromDb.getBiography());
		Assert.assertEquals("Spain", writerFromDb.getCountry());

		// remove object from DB.
		writerService.delete(writerFromDb);
	}
}
