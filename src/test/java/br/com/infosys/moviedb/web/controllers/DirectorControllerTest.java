package br.com.infosys.moviedb.web.controllers;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.httpclient.HttpStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jayway.restassured.http.ContentType;

import br.com.infosys.moviedb.MovieDbApplication;
import br.com.infosys.moviedb.core.services.DirectorService;
import br.com.infosys.moviedb.domain.entities.Director;

/**
 * Test case for the Director REST controller.
 * 
 * @author vitor191291@gmail.com
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MovieDbApplication.class)
@WebIntegrationTest
public class DirectorControllerTest {

	private static final String URL_DIRECTOR = "http://localhost:8080/v1/director";

	@Autowired
	private DirectorService directorService;

	/**
	 * Create a director.
	 */
	@Test
	public void createDirector() {
		// build request data
		Director director = TestUtil.createDirector("João");

		// invoke API to post the resource and assert the response
		given().
				contentType(ContentType.JSON).
				body(director).
		when().
				post(URL_DIRECTOR).
		then().
				statusCode(HttpStatus.SC_CREATED).
				body("name", equalTo(director.getName())).
				body("biography", equalTo(director.getBiography())).
				body("country", equalTo(director.getCountry()));

		// remove object from DB.
		directorService.deleteAll();
	}
	
	/**
	 * Update a director.
	 */
	@Test
	public void updateDirector() {
		// build request data
		Director director = TestUtil.createDirector("João");
		directorService.save(director);
		
		Long idDirector = director.getIdDirector();
		
		Director changedDirector = new Director();
		changedDirector.setIdDirector(idDirector);
		changedDirector.setName("Changed name");
		changedDirector.setBiography("Changed biography");
		changedDirector.setCountry("Changed country");

		// invoke API to post the resource and assert the response
		given().
				contentType(ContentType.JSON).
				body(changedDirector).
		when().
				put(URL_DIRECTOR + "/" + idDirector).
		then().
				statusCode(HttpStatus.SC_OK).
				body("name", equalTo(changedDirector.getName())).
				body("biography", equalTo(changedDirector.getBiography())).
				body("country", equalTo(changedDirector.getCountry()));				
		
		// remove object from DB
		directorService.deleteAll();
	}

	/**
	 * Remove a director.
	 */
	@Test
	public void deleteDirector() {
		// build request data
		Director director = TestUtil.createDirector("João");
		directorService.save(director);
		
		Long idDirector = director.getIdDirector();

		// invoke API to delete the resource and assert the response
		when().
				delete(URL_DIRECTOR + "/" + idDirector).
		then(). 
				statusCode(HttpStatus.SC_NO_CONTENT);

		// try to fetch directly from DB
		Director directorFromDb = directorService.findById(idDirector);

		// assert that there is no data found
		assertNull(directorFromDb);
	}

	/**
	 * Remove all directors.
	 */
	@Test
	public void deleteAllDirectors() {
		// build request data
		Director director = TestUtil.createDirector("João");
		Director director2 = TestUtil.createDirector("Vitor");
		directorService.save(Arrays.asList(director, director2));

		// invoke API to delete the resource and assert the response
		when().
				delete(URL_DIRECTOR).
		then(). 
				statusCode(HttpStatus.SC_NO_CONTENT);

		// try to fetch directly from DB
		List<Director> directorsFromDb = directorService.findAll();

		// assert that there is no data found
		assertTrue(directorsFromDb.isEmpty());
	}

	/**
	 * Retrieve a director.
	 */
	@Test
	public void getDirector() {
		// build request data
		Director director = TestUtil.createDirector("João");
		directorService.save(director);
		
		Long idDirector = director.getIdDirector();
		
		// invoke API to delete the resource and assert the response
		when().
				get(URL_DIRECTOR + "/" + idDirector).
		then().
				statusCode(HttpStatus.SC_OK).
				body("idDirector", equalTo(director.getIdDirector().intValue())).
				body("name", equalTo(director.getName())).
				body("biography", equalTo(director.getBiography())).
				body("country", equalTo(director.getCountry())).
				body("version", equalTo(director.getVersion()));

		// remove object from DB.
		directorService.delete(director);
	}

	/**
	 * Retrieve all directors.
	 */
	@Test
	public void getAllDirectors() {
		// build request data
		Director director = TestUtil.createDirector("João");
		Director director2 = TestUtil.createDirector("Vitor");
		directorService.save(Arrays.asList(director, director2));

		// invoke API to delete the resource and assert the response
		when().
				get(URL_DIRECTOR).
		then().
				statusCode(HttpStatus.SC_OK).
				body("size()", is(2));

		// remove object from DB.
		directorService.deleteAll();
	}

}
