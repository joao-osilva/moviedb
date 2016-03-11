package br.com.infosys.moviedb.web.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.infosys.moviedb.core.services.MovieService;
import br.com.infosys.moviedb.domain.entities.Movie;

@RestController
@RequestMapping("${api.url.movie}")
public class MovieController {

	private static final Logger logger = LoggerFactory.getLogger(MovieController.class);

	private MovieService movieService;

	@Autowired
	public MovieController(MovieService movieService) {
		this.movieService = movieService;
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Movie> createMovie(@RequestBody Movie movie) {
		logger.info("Creating a new Movie: " + movie.getTitle());

		if (movieService.exists(movie.getIdMovie())) {
			logger.info("A Movie with id: " + movie.getIdMovie() + " already exist!");
			return new ResponseEntity<Movie>(HttpStatus.CONFLICT);
		}

		Movie persistedMovie = movieService.save(movie);

		return new ResponseEntity<Movie>(persistedMovie, HttpStatus.CREATED);
	}

}
