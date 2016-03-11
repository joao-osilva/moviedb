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

import br.com.infosys.moviedb.core.services.ActorService;
import br.com.infosys.moviedb.domain.entities.Actor;

@RestController
@RequestMapping("/v1/actor")
public class ActorController {

	private static final Logger logger = LoggerFactory.getLogger(ActorController.class);

	private ActorService actorService;

	@Autowired
	public ActorController(ActorService actorService) {
		this.actorService = actorService;
	}

	@RequestMapping(path = "/", 
			        method = RequestMethod.POST, 
			        consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Actor> createActor(@RequestBody Actor actor) {
		logger.info("Creating a new Actor: " + actor.getName());
		
		if (actorService.exists(actor.getIdActor())) {
			logger.info("A Actor with id: " + actor.getIdActor() + " already exist!");
			return new ResponseEntity<Actor>(HttpStatus.CONFLICT);			
		}
		
		Actor persistedActor = actorService.save(actor);
		
		return new ResponseEntity<Actor>(persistedActor, HttpStatus.CREATED);
	}

}
