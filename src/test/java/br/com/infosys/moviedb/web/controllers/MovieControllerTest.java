package br.com.infosys.moviedb.web.controllers;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
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

		Movie response = restTemplate.postForObject(URL_MOVIE, movie, Movie.class);

		// assert the response
		Assert.assertNotNull(response);

		Movie movieFromDb = movieService.findById(response.getIdMovie());
		Assert.assertEquals("Persona", movieFromDb.getTitle());
		Assert.assertEquals(movie.getDirector(), movieFromDb.getDirector());
		Assert.assertEquals(movie.getWriters(), movieFromDb.getWriters());
		Assert.assertEquals(movie.getCast(), movieFromDb.getCast());
		Assert.assertEquals(movie.getGenre(), movieFromDb.getGenre());
		Assert.assertEquals(movie.getPlotSummary(), movieFromDb.getPlotSummary());
		Assert.assertEquals(movie.getCountry(), movieFromDb.getCountry());
		Assert.assertEquals(movie.getLanguage(), movieFromDb.getLanguage());
		Assert.assertEquals(movie.getReleaseDate(), movieFromDb.getReleaseDate());

		// remove object from DB.		
		movieService.delete(movieFromDb);
		directorService.delete(movie.getDirector());
		writerService.delete(movie.getWriters());
		actorService.delete(movie.getCast());		
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

		// invoke API to delete the resource
		restTemplate.delete(URL_MOVIE + "/" + idMovie);

		// try to fetch directly from DB
		Movie movieFromDb = movieService.findById(idMovie);

		// assert that there is no data found
		Assert.assertNull(movieFromDb);
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

		// invoke API to delete the resource
		restTemplate.delete(URL_MOVIE);

		// try to fetch directly from DB
		List<Movie> moviesFromDb = movieService.findAll();

		// assert that there is no data found
		Assert.assertTrue(moviesFromDb.isEmpty());
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
		Assert.assertNotNull(response);

		Assert.assertEquals(movie.getIdMovie(), response.getIdMovie());
		Assert.assertEquals(movie.getTitle(), response.getTitle());
		Assert.assertEquals(movie.getDirector(), response.getDirector());
		Assert.assertEquals(movie.getWriters(), response.getWriters());
		Assert.assertEquals(movie.getCast(), response.getCast());
		Assert.assertEquals(movie.getGenre(), response.getGenre());
		Assert.assertEquals(movie.getPlotSummary(), response.getPlotSummary());
		Assert.assertEquals(movie.getCountry(), response.getCountry());
		Assert.assertEquals(movie.getLanguage(), response.getLanguage());
		//TODO fix date Assert.assertEquals(movie.getReleaseDate(), response.getReleaseDate());
		Assert.assertEquals(movie.getVersion(), response.getVersion());
		
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

		// invoke API to delete the resource
		List<?> response = restTemplate.getForObject(URL_MOVIE, List.class);

		// assert the response
		Assert.assertNotNull(response);

		Assert.assertTrue(response.size() == 2);
		
		// remove object from DB.
		movieService.deleteAll();
	}

}
