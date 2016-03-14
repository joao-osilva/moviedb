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

import br.com.infosys.moviedb.core.services.ActorService;
import br.com.infosys.moviedb.domain.entities.Actor;

@RestController
@RequestMapping("${api.url.actor}")
public class ActorController {

	private static final Logger logger = LoggerFactory.getLogger(ActorController.class);

	private ActorService actorService;

	@Autowired
	public ActorController(ActorService actorService) {
		this.actorService = actorService;
	}

	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Actor> createActor(@RequestBody Actor actor) {

		logger.info("Creating Actor with name " + actor.getName());
		if (actorService.exists(actor.getIdActor())) {
			logger.info("Actor with id " + actor.getIdActor() + " already exist!");
			return new ResponseEntity<Actor>(HttpStatus.CONFLICT);
		}

		Actor persistedActor = actorService.save(actor);

		return new ResponseEntity<Actor>(persistedActor, HttpStatus.CREATED);
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Actor> deleteActor(@PathVariable("id") Long id) {
		logger.info("Deleting Actor " + id);

		if (!actorService.exists(id)) {
			logger.info("Actor with id " + id + " not found!");
			return new ResponseEntity<Actor>(HttpStatus.NOT_FOUND);
		}

		actorService.deleteById(id);

		return new ResponseEntity<Actor>(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity<Actor> deleteAllActors() {
		logger.info("Deleting all Actors");

		actorService.deleteAll();

		return new ResponseEntity<Actor>(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Actor> getActor(@PathVariable("id") Long id) {
		logger.info("Fetching Actor with id " + id);

		Actor actor = actorService.findById(id);

		if (actor == null) {
			logger.info("Actor with id " + id + " not found!");
			return new ResponseEntity<Actor>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Actor>(actor, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Actor>> getAllActors() {
		logger.info("Fetching all Actors");

		List<Actor> actors = actorService.findAll();

		if (actors.isEmpty()) {
			return new ResponseEntity<List<Actor>>(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<List<Actor>>(actors, HttpStatus.OK);
	}

}
