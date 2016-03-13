package br.com.infosys.moviedb.web.controllers;

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
import br.com.infosys.moviedb.domain.entities.Actor;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MovieDbApplication.class)
@WebIntegrationTest
public class ActorControllerTest {

	private static final String URL_ACTOR = "http://localhost:8080/v1/actor";

	@Autowired
	private ActorService actorService;

	private RestTemplate restTemplate = new TestRestTemplate();

	@Test
	public void createActor() {
		// build request data
		Actor actor = TestUtil.createActor("João");

		Actor response = restTemplate.postForObject(URL_ACTOR, actor, Actor.class);

		// assert the response
		Assert.assertNotNull(response);

		Actor actorFromDb = actorService.findById(response.getIdActor());
		Assert.assertEquals("João", actorFromDb.getName());
		Assert.assertEquals(actor.getBiography(), actorFromDb.getBiography());
		Assert.assertEquals(actor.getCountry(), actorFromDb.getCountry());

		// remove object from DB.
		actorService.delete(actorFromDb);
	}

	@Test
	public void deleteActor() {
		// build request data
		Actor actor = TestUtil.createActor("João");

		actorService.save(actor);
		Long idActor = actor.getIdActor();

		// invoke API to delete the resource
		restTemplate.delete(URL_ACTOR + "/" + idActor);

		// try to fetch directly from DB
		Actor actorFromDb = actorService.findById(idActor);

		// assert that there is no data found
		Assert.assertNull(actorFromDb);
	}

	@Test
	public void deleteAllActors() {
		// build request data
		Actor actor = TestUtil.createActor("João");
		Actor actor2 = TestUtil.createActor("Vitor");

		actorService.save(Arrays.asList(actor, actor2));

		// invoke API to delete the resource
		restTemplate.delete(URL_ACTOR);

		// try to fetch directly from DB
		List<Actor> actorsFromDb = actorService.findAll();

		// assert that there is no data found
		Assert.assertTrue(actorsFromDb.isEmpty());
		;
	}
}
