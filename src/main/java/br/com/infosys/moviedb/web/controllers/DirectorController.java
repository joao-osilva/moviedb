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

import br.com.infosys.moviedb.core.services.DirectorService;
import br.com.infosys.moviedb.domain.entities.Director;

/**
 * Director resource API.
 * 
 * @author vitor191291@gmail.com
 *
 */
@RestController
@RequestMapping("${api.url.director}")
public class DirectorController {

	private static final Logger logger = LoggerFactory.getLogger(DirectorController.class);

	private DirectorService directorService;

	@Autowired
	public DirectorController(DirectorService directorService) {
		this.directorService = directorService;
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Director> createDirector(@RequestBody Director director) {
		logger.info("Creating Director with name " + director.getName());

		if (directorService.exists(director.getIdDirector())) {
			logger.info("Director with id " + director.getIdDirector() + " already exist!");
			return new ResponseEntity<Director>(HttpStatus.CONFLICT);
		}

		Director persistedDirector = directorService.save(director);

		return new ResponseEntity<Director>(persistedDirector, HttpStatus.CREATED);
	}
	
	@RequestMapping(path = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Director> updateDirector(@PathVariable("id") Long id, @RequestBody Director director) {
		logger.info("Updating Director " + id);
		
		if (!directorService.exists(id)) {
			logger.info("Director with id " + id + " not found!");
			return new ResponseEntity<Director>(HttpStatus.NOT_FOUND);
		}
		
		Director updatedDirector = directorService.update(id, director);
		
		return new ResponseEntity<Director>(updatedDirector, HttpStatus.OK);
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Director> deleteDirector(@PathVariable("id") Long id) {
		logger.info("Deleting Director " + id);

		if (!directorService.exists(id)) {
			logger.info("Director with id " + id + " not found!");
			return new ResponseEntity<Director>(HttpStatus.NOT_FOUND);
		}

		directorService.deleteById(id);

		return new ResponseEntity<Director>(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity<Director> deleteAllDirectors() {
		logger.info("Deleting all Directors");

		directorService.deleteAll();

		return new ResponseEntity<Director>(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Director> getDirector(@PathVariable("id") Long id) {
		logger.info("Fetching Director with id " + id);

		Director director = directorService.findById(id);

		if (director == null) {
			logger.info("Director with id " + id + " not found!");
			return new ResponseEntity<Director>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Director>(director, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Director>> getAllDirectors() {
		logger.info("Fetching all Directors");

		List<Director> directors = directorService.findAll();

		if (directors.isEmpty()) {
			return new ResponseEntity<List<Director>>(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<List<Director>>(directors, HttpStatus.OK);
	}

}
