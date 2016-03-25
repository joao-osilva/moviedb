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
import br.com.infosys.moviedb.core.services.WriterService;
import br.com.infosys.moviedb.domain.entities.Writer;

/**
 * Test case for the Writer REST controller.
 * 
 * @author vitor191291@gmail.com
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MovieDbApplication.class)
@WebIntegrationTest
public class WriterControllerTest {

	private static final String URL_WRITER = "http://localhost:8080/v1/writer";

	@Autowired
	private WriterService writerService;

	/**
	 * Create a writer.
	 */
	@Test
	public void createWriter() {
		// build request data
		Writer writer = TestUtil.createWriter("Marco");

		// invoke API to post the resource and assert the response
		given().
				contentType(ContentType.JSON).
				body(writer).
		when().
				post(URL_WRITER).
		then().
				statusCode(HttpStatus.SC_CREATED).
				body("name", equalTo(writer.getName())).
				body("biography", equalTo(writer.getBiography())).
				body("country", equalTo(writer.getCountry()));

		// remove object from DB.
		writerService.deleteAll();;
	}
	
	/**
	 * Update a writer.
	 */
	@Test
	public void updateWriter() {
		// build request data
		Writer writer = TestUtil.createWriter("João");
		writerService.save(writer);
		
		Long idWriter = writer.getIdWriter();
		
		Writer changedWriter = new Writer();
		changedWriter.setIdWriter(idWriter);
		changedWriter.setName("Changed name");
		changedWriter.setBiography("Changed biography");
		changedWriter.setCountry("Changed country");

		// invoke API to post the resource and assert the response
		given().
				contentType(ContentType.JSON).
				body(changedWriter).
		when().
				put(URL_WRITER + "/" + idWriter).
		then().
				statusCode(HttpStatus.SC_OK).
				body("name", equalTo(changedWriter.getName())).
				body("biography", equalTo(changedWriter.getBiography())).
				body("country", equalTo(changedWriter.getCountry()));				
		
		// remove object from DB
		writerService.deleteAll();
	}

	/**
	 * Remove a writer.
	 */
	@Test
	public void deleteWriter() {
		// build request data
		Writer writer = TestUtil.createWriter("João");
		writerService.save(writer);
		
		Long idWriter = writer.getIdWriter();

		// invoke API to delete the resource and assert the response
		when().
				delete(URL_WRITER + "/" + idWriter).
		then(). 
				statusCode(HttpStatus.SC_NO_CONTENT);

		// try to fetch directly from DB
		Writer writerFromDb = writerService.findById(idWriter);

		// assert that there is no data found
		assertNull(writerFromDb);
	}

	/**
	 * Remove all writers.
	 */
	@Test
	public void deleteAllWriters() {
		// build request data
		Writer writer = TestUtil.createWriter("João");
		Writer writer2 = TestUtil.createWriter("Vitor");
		writerService.save(Arrays.asList(writer, writer2));

		// invoke API to delete the resource and assert the response
		when().
				delete(URL_WRITER).
		then(). 
				statusCode(HttpStatus.SC_NO_CONTENT);

		// try to fetch directly from DB
		List<Writer> writersFromDb = writerService.findAll();

		// assert that there is no data found
		assertTrue(writersFromDb.isEmpty());
	}
	
	/**
	 * Retrieve a writer.
	 */
	@Test
	public void getWriter() {
		// build request data
		Writer writer = TestUtil.createWriter("João");
		writerService.save(writer);
		
		Long idWriter = writer.getIdWriter();
		
		// invoke API to get the resource and assert the response
		when().
				get(URL_WRITER + "/" + idWriter).
		then().
				statusCode(HttpStatus.SC_OK).
				body("idWriter", equalTo(writer.getIdWriter().intValue())).
				body("name", equalTo(writer.getName())).
				body("biography", equalTo(writer.getBiography())).
				body("country", equalTo(writer.getCountry())).
				body("version", equalTo(writer.getVersion()));
		
		// remove object from DB.
		writerService.delete(writer);
	}
	
	/**
	 * Retrieve all writers.
	 */
	@Test
	public void getAllWriters() {
		// build request data
		Writer writer = TestUtil.createWriter("João");
		Writer writer2 = TestUtil.createWriter("Vitor");
		writerService.save(Arrays.asList(writer, writer2));

		// invoke API to delete the resource and assert the response
		when().
				get(URL_WRITER).
		then().
				statusCode(HttpStatus.SC_OK).
				body("size()", is(2));
		
		// remove object from DB.
		writerService.deleteAll();
	}
}
