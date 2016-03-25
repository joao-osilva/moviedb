package br.com.infosys.moviedb.web.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.infosys.moviedb.core.services.MovieService;
import br.com.infosys.moviedb.domain.entities.Movie;

/**
 * Movie resource API.
 * 
 * @author vitor191291@gmail.com
 *
 */
@RestController
@RequestMapping("${api.url.movie}")
public class MovieController {

	private static final Logger logger = LoggerFactory.getLogger(MovieController.class);

	private MovieService movieService;

	@Autowired
	public MovieController(MovieService movieService) {
		this.movieService = movieService;
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Movie> createMovie(@RequestBody Movie movie) {
		logger.info("Creating a new Movie: " + movie.getTitle());

		if (movieService.exists(movie.getIdMovie())) {
			logger.info("Movie with id: " + movie.getIdMovie() + " already exist!");
			return new ResponseEntity<Movie>(HttpStatus.CONFLICT);
		}

		Movie persistedMovie = movieService.save(movie);

		return new ResponseEntity<Movie>(persistedMovie, HttpStatus.CREATED);
	}
	
	@RequestMapping(path = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Movie> updateMovie(@PathVariable("id") Long id, @RequestBody Movie movie) {
		logger.info("Updating Movie " + id);
		
		if (!movieService.exists(id)) {
			logger.info("Movie with id " + id + " not found!");
			return new ResponseEntity<Movie>(HttpStatus.NOT_FOUND);
		}
		
		Movie updatedMovie = movieService.update(id, movie);
		
		return new ResponseEntity<Movie>(updatedMovie, HttpStatus.OK);
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Movie> deleteMovie(@PathVariable("id") Long id) {
		logger.info("Deleting Movie: " + id);

		if (!movieService.exists(id)) {
			logger.info("Movie with id: " + id + " not found!");
			return new ResponseEntity<Movie>(HttpStatus.NOT_FOUND);
		}

		movieService.deleteById(id);

		return new ResponseEntity<Movie>(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity<Movie> deleteAllMovies() {
		logger.info("Deleting all Movies");

		movieService.deleteAll();

		return new ResponseEntity<Movie>(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Movie> getMovie(@PathVariable("id") Long id) {
		logger.info("Fetching Movie with id " + id);

		Movie movie = movieService.findById(id);

		if (movie == null) {
			logger.info("Movie with id " + id + " not found!");
			return new ResponseEntity<Movie>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Movie>(movie, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Movie>> getAllMovies() {
		logger.info("Fetching all Movies");

		List<Movie> movies = movieService.findAll();

		if (movies.isEmpty()) {
			return new ResponseEntity<List<Movie>>(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<List<Movie>>(movies, HttpStatus.OK);
	}

}
