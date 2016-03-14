package br.com.infosys.moviedb.web.controllers;

import static com.jayway.restassured.RestAssured.when;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.httpclient.HttpStatus;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import br.com.infosys.moviedb.MovieDbApplication;
import br.com.infosys.moviedb.core.services.ActorService;
import br.com.infosys.moviedb.core.services.DirectorService;
import br.com.infosys.moviedb.core.services.MovieService;
import br.com.infosys.moviedb.core.services.WriterService;
import br.com.infosys.moviedb.domain.entities.Actor;
import br.com.infosys.moviedb.domain.entities.Director;
import br.com.infosys.moviedb.domain.entities.Movie;
import br.com.infosys.moviedb.domain.entities.Writer;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MovieDbApplication.class)
@WebIntegrationTest
public class MovieControllerTest {

	private static final String URL_MOVIE = "http://localhost:8080/v1/movie";

	@Autowired
	private MovieService movieService;

	@Autowired
	private DirectorService directorService;

	@Autowired
	private WriterService writerService;

	@Autowired
	private ActorService actorService;

	private RestTemplate restTemplate = new TestRestTemplate();
	
	@Test
	public void createMovie() throws ParseException {
		// build request data
		Director director = TestUtil.createDirector("Andrei Takovsky");
		directorService.save(director);

		Writer writer = TestUtil.createWriter("Andrei Takovsky");
		Writer writer2 = TestUtil.createWriter("Aleksandr Misharin");
		writerService.save(Arrays.asList(writer, writer2));

		Actor actor = TestUtil.createActor("Margarita Terekhova");
		Actor actor2 = TestUtil.createActor("Filipp Yankovskiy");
		actorService.save(Arrays.asList(actor, actor2));

		Movie movie = TestUtil.createMovie("Persona", 
										   director, 
										   Arrays.asList(writer, writer2), 
										   Arrays.asList(actor, actor2));
		
		// invoke API to post the resource and assert the response
		Movie response = restTemplate.postForObject(URL_MOVIE, movie, Movie.class);

		// assert the response
		assertNotNull(response);

		Movie movieFromDb = movieService.findById(response.getIdMovie());
		assertEquals(movie.getTitle(), movieFromDb.getTitle());
		assertEquals(movie.getDirector(), movieFromDb.getDirector());
		assertEquals(movie.getWriters(), movieFromDb.getWriters());
		assertEquals(movie.getCast(), movieFromDb.getCast());
		assertEquals(movie.getGenre(), movieFromDb.getGenre());
		assertEquals(movie.getPlotSummary(), movieFromDb.getPlotSummary());
		assertEquals(movie.getCountry(), movieFromDb.getCountry());
		assertEquals(movie.getLanguage(), movieFromDb.getLanguage());
		assertEquals(movie.getReleaseDate(), movieFromDb.getReleaseDate());

		// remove object from DB.		
		movieService.deleteAll();
		directorService.deleteAll();
		writerService.deleteAll();
		actorService.deleteAll();
	}

	@Test
	public void deleteMovie() throws ParseException {
		// build request data
		Director director = TestUtil.createDirector("Andrei Takovsky");
		directorService.save(director);

		Writer writer = TestUtil.createWriter("Andrei Takovsky");
		Writer writer2 = TestUtil.createWriter("Aleksandr Misharin");
		writerService.save(Arrays.asList(writer, writer2));

		Actor actor = TestUtil.createActor("Margarita Terekhova");
		Actor actor2 = TestUtil.createActor("Filipp Yankovskiy");
		actorService.save(Arrays.asList(actor, actor2));

		Movie movie = TestUtil.createMovie("Persona", 
				  						   director, 
				  						   Arrays.asList(writer, writer2),
				  						   Arrays.asList(actor, actor2));

		movieService.save(movie);
		
		Long idMovie = movie.getIdMovie();

		// invoke API to delete the resource and assert the response
		when().
				delete(URL_MOVIE + "/" + idMovie).
		then(). 
				statusCode(HttpStatus.SC_NO_CONTENT);

		// try to fetch directly from DB
		Movie movieFromDb = movieService.findById(idMovie);

		// assert that there is no data found
		assertNull(movieFromDb);
	}

	@Test
	public void deleteAllMovies() throws ParseException {
		// build request data
		Director director = TestUtil.createDirector("Andrei Takovsky");
		directorService.save(director);

		Writer writer = TestUtil.createWriter("Andrei Takovsky");
		Writer writer2 = TestUtil.createWriter("Aleksandr Misharin");
		writerService.save(Arrays.asList(writer, writer2));

		Actor actor = TestUtil.createActor("Margarita Terekhova");
		Actor actor2 = TestUtil.createActor("Filipp Yankovskiy");
		actorService.save(Arrays.asList(actor, actor2));

		Movie movie = TestUtil.createMovie("Persona", 
				  						   director, 
				  						   Arrays.asList(writer, writer2),
				  						   Arrays.asList(actor, actor2));
		
		Movie movie2 = TestUtil.createMovie("Eyes Wide Shut", 
				   							director, 
				   							Arrays.asList(writer, writer2),
				   							Arrays.asList(actor, actor2));

		movieService.save(Arrays.asList(movie, movie2));

		// invoke API to delete the resource and assert the response
		when().
				delete(URL_MOVIE).
		then(). 
				statusCode(HttpStatus.SC_NO_CONTENT);

		// try to fetch directly from DB
		List<Movie> moviesFromDb = movieService.findAll();

		// assert that there is no data found
		assertTrue(moviesFromDb.isEmpty());
	}
	
	@Test
	public void getMovie() throws ParseException {
		// build request data
		Director director = TestUtil.createDirector("Andrei Takovsky");
		directorService.save(director);

		Writer writer = TestUtil.createWriter("Andrei Takovsky");
		Writer writer2 = TestUtil.createWriter("Aleksandr Misharin");
		writerService.save(Arrays.asList(writer, writer2));

		Actor actor = TestUtil.createActor("Margarita Terekhova");
		Actor actor2 = TestUtil.createActor("Filipp Yankovskiy");
		actorService.save(Arrays.asList(actor, actor2));

		Movie movie = TestUtil.createMovie("Persona", 
				  						   director, 
				  						   Arrays.asList(writer, writer2),
				  						   Arrays.asList(actor, actor2));

		movieService.save(movie);

		// invoke API to delete the resource
		Movie response = restTemplate.getForObject(URL_MOVIE + "/" + movie.getIdMovie(), Movie.class);

		// assert the response
		assertNotNull(response);

		assertEquals(movie.getIdMovie(), response.getIdMovie());
		assertEquals(movie.getTitle(), response.getTitle());
		assertEquals(movie.getDirector(), response.getDirector());
		assertEquals(movie.getWriters(), response.getWriters());
		assertEquals(movie.getCast(), response.getCast());
		assertEquals(movie.getGenre(), response.getGenre());
		assertEquals(movie.getPlotSummary(), response.getPlotSummary());
		assertEquals(movie.getCountry(), response.getCountry());
		assertEquals(movie.getLanguage(), response.getLanguage());
		//TODO fix date Assert.assertEquals(movie.getReleaseDate(), response.getReleaseDate());
		assertEquals(movie.getVersion(), response.getVersion());
		
		// remove object from DB.
		movieService.delete(response);
		directorService.delete(movie.getDirector());
		writerService.delete(movie.getWriters());
		actorService.delete(movie.getCast());
	}
	
	@Test
	public void getAllMovies() throws ParseException {
		// build request data
		Director director = TestUtil.createDirector("Andrei Takovsky");
		directorService.save(director);

		Writer writer = TestUtil.createWriter("Andrei Takovsky");
		Writer writer2 = TestUtil.createWriter("Aleksandr Misharin");
		writerService.save(Arrays.asList(writer, writer2));

		Actor actor = TestUtil.createActor("Margarita Terekhova");
		Actor actor2 = TestUtil.createActor("Filipp Yankovskiy");
		actorService.save(Arrays.asList(actor, actor2));

		Movie movie = TestUtil.createMovie("Persona", 
				  						   director, 
				  						   Arrays.asList(writer, writer2),
				  						   Arrays.asList(actor, actor2));
		
		Movie movie2 = TestUtil.createMovie("Eyes Wide Shut", 
				   							director, 
				   							Arrays.asList(writer, writer2),
				   							Arrays.asList(actor, actor2));

		movieService.save(Arrays.asList(movie, movie2));

		// invoke API to delete the resource and assert the response
		when().
				get(URL_MOVIE).
		then().
				statusCode(HttpStatus.SC_OK).
				body("size()", is(2));
		
		// remove object from DB.
		movieService.deleteAll();
	}

}
