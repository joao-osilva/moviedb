package br.com.infosys.moviedb.web.controllers;

import static com.jayway.restassured.RestAssured.when;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.httpclient.HttpStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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

/**
 * Test case for the Movie REST controller.
 * 
 * @author vitor191291@gmail.com
 *
 */
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
	
	/**
	 * Create a movie.
	 * 
	 * @throws ParseException in case something goes wrong with the date conversion.
	 */
	@Test
	public void createMovie() throws ParseException {
		// build request data
		Movie movie = getAlreadyDoneMovie();
		
		// invoke API to post the resource and assert the response
		ResponseEntity<Movie> response = restTemplate.postForEntity(URL_MOVIE, movie, Movie.class, Collections.emptyMap());

		// assert the response
		assertEquals(HttpStatus.SC_CREATED, response.getStatusCode().value());

		Movie movieFromDb = movieService.findById(response.getBody().getIdMovie());
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
	
	/**
	 * Update a movie.
	 * 
	 * @throws ParseException in case something goes wrong with the date conversion.
	 */
	@Test
	public void updateMovie() throws ParseException {
		// build request data
		Movie movie = getAlreadyDoneMovie();
		movieService.save(movie);
		
		Long idMovie = movie.getIdMovie();
		
		Director changedDirector = TestUtil.createDirector("Changed director");
		directorService.save(changedDirector);

		Writer changedWriter = TestUtil.createWriter("Changed writer");
		Writer changedWriter2 = TestUtil.createWriter("Changed writer 2");
		writerService.save(Arrays.asList(changedWriter, changedWriter2));

		Actor changedActor = TestUtil.createActor("Changed actor");
		Actor changedActor2 = TestUtil.createActor("Changed actor 2");
		actorService.save(Arrays.asList(changedActor, changedActor2));

		Movie changedMovie = TestUtil.createMovie("Changed movie", 
				  						   		  changedDirector, 
				  						   		  Arrays.asList(changedWriter, changedWriter2),
				  						   		  Arrays.asList(changedActor, changedActor2));		

		// invoke API to post the resource and assert the response
		ResponseEntity<Movie> response = restTemplate.exchange(URL_MOVIE + "/" + idMovie, 
													    	   HttpMethod.PUT, 
													    	   new HttpEntity<Movie>(changedMovie), 
													    	   Movie.class, 
													    	   Collections.emptyMap());
		
		// assert the response
		assertEquals(HttpStatus.SC_OK, response.getStatusCode().value());
		
		Movie responseMovie = (Movie) response.getBody();
		assertEquals(responseMovie.getTitle(), changedMovie.getTitle());
		assertEquals(responseMovie.getDirector(), changedMovie.getDirector());
		assertEquals(responseMovie.getWriters(), changedMovie.getWriters());
		assertEquals(responseMovie.getCast(), changedMovie.getCast());		
		
		// remove object from DB
		movieService.deleteAll();
	}

	/**
	 * Remove a movie.
	 * 
	 * @throws ParseException in case something goes wrong with the date conversion.
	 */
	@Test
	public void deleteMovie() throws ParseException {
		// build request data
		Movie movie = getAlreadyDoneMovie();

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

	/**
	 * Remove all movies.
	 * 
	 * @throws ParseException in case something goes wrong with the date conversion.
	 */
	@Test
	public void deleteAllMovies() throws ParseException {
		// build request data
		Movie movie = getAlreadyDoneMovie();
		
		Movie movie2 = getAlreadyDoneMovie("Eyes Wide Shut");

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
	
	/**
	 * Retrieve a movie.
	 * 
	 * @throws ParseException in case something goes wrong with the date conversion.
	 */
	@Test
	public void getMovie() throws ParseException {
		// build request data
		Movie movie = getAlreadyDoneMovie();

		movieService.save(movie);

		// invoke API to delete the resource
		ResponseEntity<Movie> response = restTemplate.getForEntity(URL_MOVIE + "/" + movie.getIdMovie(), Movie.class, Collections.emptyMap());

		// assert the response
		assertEquals(HttpStatus.SC_OK, response.getStatusCode().value());

		Movie responseMovie = response.getBody();
		assertEquals(movie.getIdMovie(), responseMovie.getIdMovie());
		assertEquals(movie.getTitle(), responseMovie.getTitle());
		assertEquals(movie.getDirector(), responseMovie.getDirector());
		assertEquals(movie.getWriters(), responseMovie.getWriters());
		assertEquals(movie.getCast(), responseMovie.getCast());
		assertEquals(movie.getGenre(), responseMovie.getGenre());
		assertEquals(movie.getPlotSummary(), responseMovie.getPlotSummary());
		assertEquals(movie.getCountry(), responseMovie.getCountry());
		assertEquals(movie.getLanguage(), responseMovie.getLanguage());
		//TODO fix date Assert.assertEquals(responseMovie.getReleaseDate(), response.getReleaseDate());
		assertEquals(movie.getVersion(), responseMovie.getVersion());
		
		// remove object from DB.
		movieService.delete(responseMovie);
		directorService.delete(movie.getDirector());
		writerService.delete(movie.getWriters());
		actorService.delete(movie.getCast());
	}
	
	/**
	 * Retrieve all movies.
	 * 
	 * @throws ParseException in case something goes wrong with the date conversion.
	 */
	@Test
	public void getAllMovies() throws ParseException {
		// build request data
		Movie movie = getAlreadyDoneMovie();
		
		Movie movie2 = getAlreadyDoneMovie("Eyes Wide Shut");

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
	
	/**
	 * Auxiliary method for creating a movie object.
	 * 
	 * @param args
	 * 
	 * @return movie.
	 * 
	 * @throws ParseException in case something goes wrong with the date conversion.
	 */
	private Movie getAlreadyDoneMovie(String... args) throws ParseException {
		Director director = TestUtil.createDirector("Andrei Takovsky");
		directorService.save(director);

		Writer writer = TestUtil.createWriter("Andrei Takovsky");
		Writer writer2 = TestUtil.createWriter("Aleksandr Misharin");
		writerService.save(Arrays.asList(writer, writer2));

		Actor actor = TestUtil.createActor("Margarita Terekhova");
		Actor actor2 = TestUtil.createActor("Filipp Yankovskiy");
		actorService.save(Arrays.asList(actor, actor2));
		
		String argsValue = "";
		if (args.length != 0) {
			argsValue = args[0];
		}
		
		Movie movie = TestUtil.createMovie(argsValue.trim().isEmpty() ? "Persona" : argsValue, 
				  						   director, 
				  						   Arrays.asList(writer, writer2),
				  						   Arrays.asList(actor, actor2));
		
		return movie;
	}

}
