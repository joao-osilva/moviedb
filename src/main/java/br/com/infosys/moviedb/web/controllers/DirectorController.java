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

import br.com.infosys.moviedb.core.services.DirectorService;
import br.com.infosys.moviedb.domain.entities.Director;

@RestController
@RequestMapping("${api.url.director}")
public class DirectorController {

	private static final Logger logger = LoggerFactory.getLogger(DirectorController.class);

	private DirectorService directorService;

	@Autowired
	public DirectorController(DirectorService directorService) {
		this.directorService = directorService;
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Director> createDirector(@RequestBody Director director) {
		logger.info("Creating a new Director: " + director.getName());

		if (directorService.exists(director.getIdDirector())) {
			logger.info("A Director with id: " + director.getIdDirector() + " already exist!");
			return new ResponseEntity<Director>(HttpStatus.CONFLICT);
		}

		Director persistedDirector = directorService.save(director);

		return new ResponseEntity<Director>(persistedDirector, HttpStatus.CREATED);
	}

}
