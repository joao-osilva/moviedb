package br.com.infosys.moviedb.web.controllers;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
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
import br.com.infosys.moviedb.core.services.MovieService;
import br.com.infosys.moviedb.core.services.WriterService;
import br.com.infosys.moviedb.domain.entities.Actor;
import br.com.infosys.moviedb.domain.entities.Director;
import br.com.infosys.moviedb.domain.entities.Movie;
import br.com.infosys.moviedb.domain.entities.Writer;
import br.com.infosys.moviedb.domain.enums.GenreEnum;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MovieDbApplication.class)
@WebIntegrationTest
public class MovieControllerTest {
	
	private static final String URL_MOVIE = "http://localhost:8080/v1/movie";

	@Autowired
	private MovieService movieService;

	private RestTemplate restTemplate = new TestRestTemplate();
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	@Test
	public void createMovie() throws JsonProcessingException, ParseException {
		// build request data
		Director director = new Director();
		director.setName("Andrei Takovsky");
		director.setBiography("A brief biography of Andrei Tarkovsky.");
		director.setCountry("Russia");
		
		Writer writer = new Writer();
		writer.setName("Andrei Takovsky");
		writer.setBiography("A brief biography of Andrei Takovsky.");
		writer.setCountry("Russia");	
		
		Writer writer2 = new Writer();
		writer2.setName("Aleksandr Misharin");
		writer2.setBiography("A brief biography of Aleksandr Misharin.");
		writer2.setCountry("Russia");
		
		Actor actor = new Actor();
		actor.setName("Margarita Terekhova");
		actor.setBiography("A brief biography of Margarita Terekhova.");
		actor.setCountry("Russia");
		
		Actor actor2 = new Actor();
		actor2.setName("Filipp Yankovskiy");
		actor2.setBiography("A brief biography of Filipp Yankovskiy.");
		actor2.setCountry("Russia");
		
		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("title", "Zerkalo");
		requestBody.put("director", director);
		requestBody.put("writers", new HashSet<>(Arrays.asList(writer, writer2)));
		requestBody.put("cast", new HashSet<>(Arrays.asList(actor, actor2)));
		requestBody.put("genre", GenreEnum.Drama);
		requestBody.put("plotSummary", "A brief plot summary of Zerkalo, a film by Tarkovsky.");
		requestBody.put("country", "Russia");
		requestBody.put("language", "Russian");
		requestBody.put("releaseDate", DateUtils.parseDate("1975/03/07", "yyyy/MM/dd"));		

		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);

		HttpEntity<String> httpEntity = new HttpEntity<String>(OBJECT_MAPPER.writeValueAsString(requestBody),
				requestHeaders);

		Map<String, Object> response = restTemplate.postForObject(URL_MOVIE, httpEntity, Map.class,
				Collections.EMPTY_MAP);

		// assert the response
		Assert.assertNotNull(response);

		// assert that the object is valid
		Long movieId = Long.valueOf(response.get("idMovie").toString());

		Assert.assertNotNull(movieId);

		Movie movieFromDb = movieService.findById(movieId);
//		Assert.assertEquals("Marco", writerFromDb.getName());
//		Assert.assertEquals("This is a brief test biography.", writerFromDb.getBiography());
//		Assert.assertEquals("Spain", writerFromDb.getCountry());

		// remove object from DB.
		movieService.delete(movieFromDb);
	}

}
