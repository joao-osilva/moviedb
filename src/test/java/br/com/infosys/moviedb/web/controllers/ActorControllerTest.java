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
import br.com.infosys.moviedb.core.services.ActorService;
import br.com.infosys.moviedb.domain.entities.Actor;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MovieDbApplication.class)
@WebIntegrationTest
public class ActorControllerTest {

	private static final String URL_ACTOR = "http://localhost:8080/v1/actor";

	@Autowired
	private ActorService actorService;

	@Test
	public void createActor() {
		// build request data
		Actor actor = TestUtil.createActor("João");

		// invoke API to post the resource and assert the response
		given().
				contentType(ContentType.JSON).
				body(actor).
		when().
				post(URL_ACTOR).
		then().
				statusCode(HttpStatus.SC_CREATED).
				body("name", equalTo(actor.getName())).
				body("biography", equalTo(actor.getBiography())).
				body("country", equalTo(actor.getCountry()));
		
		// remove object from DB
		actorService.deleteAll();
	}
	
	@Test
	public void updateActor() {
		// build request data
		Actor actor = TestUtil.createActor("João");
		actorService.save(actor);
		
		Long idActor = actor.getIdActor();
		
		Actor changedActor = new Actor();
		changedActor.setIdActor(idActor);
		changedActor.setName("Changed name");
		changedActor.setBiography("Changed biography");
		changedActor.setCountry("Changed country");

		// invoke API to post the resource and assert the response
		given().
				contentType(ContentType.JSON).
				body(changedActor).
		when().
				put(URL_ACTOR + "/" + idActor).
		then().
				statusCode(HttpStatus.SC_OK).
				body("name", equalTo(changedActor.getName())).
				body("biography", equalTo(changedActor.getBiography())).
				body("country", equalTo(changedActor.getCountry()));				
		
		// remove object from DB
		actorService.deleteAll();
	}

	@Test
	public void deleteActor() {
		// build request data
		Actor actor = TestUtil.createActor("João");
		actorService.save(actor);
		
		Long idActor = actor.getIdActor();

		// invoke API to delete the resource and assert the response
		when().
				delete(URL_ACTOR + "/" + idActor).
		then(). 
				statusCode(HttpStatus.SC_NO_CONTENT);

		// try to fetch directly from DB
		Actor actorFromDb = actorService.findById(idActor);

		// assert that there is no data found
		assertNull(actorFromDb);
	}

	@Test
	public void deleteAllActors() {
		// build request data
		Actor actor = TestUtil.createActor("João");
		Actor actor2 = TestUtil.createActor("Vitor");
		actorService.save(Arrays.asList(actor, actor2));

		// invoke API to delete the resource and assert the response
		when().
				delete(URL_ACTOR).
		then(). 
				statusCode(HttpStatus.SC_NO_CONTENT);

		// try to fetch directly from DB
		List<Actor> actorsFromDb = actorService.findAll();

		// assert that there is no data found
		assertTrue(actorsFromDb.isEmpty());
	}

	@Test
	public void getActor() {
		// build request data
		Actor actor = TestUtil.createActor("João");
		actorService.save(actor);
		
		Long idActor = actor.getIdActor();

		// invoke API to get the resource and assert the response
		when().
				get(URL_ACTOR + "/" + idActor).
		then().
				statusCode(HttpStatus.SC_OK).
				body("idActor", equalTo(actor.getIdActor().intValue())).
				body("name", equalTo(actor.getName())).
				body("biography", equalTo(actor.getBiography())).
				body("country", equalTo(actor.getCountry())).
				body("version", equalTo(actor.getVersion()));
		
		// remove object from DB.
		actorService.delete(actor);
	}
	
	@Test
	public void getAllActors() {
		// build request data
		Actor actor = TestUtil.createActor("João");
		Actor actor2 = TestUtil.createActor("Vitor");
		actorService.save(Arrays.asList(actor, actor2));

		// invoke API to delete the resource and assert the response
		when().
				get(URL_ACTOR).
		then().
				statusCode(HttpStatus.SC_OK).
				body("size()", is(2));
				
		// remove object from DB.
		actorService.deleteAll();
	}
}
