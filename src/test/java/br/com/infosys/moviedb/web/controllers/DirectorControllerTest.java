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
import br.com.infosys.moviedb.core.services.DirectorService;
import br.com.infosys.moviedb.domain.entities.Director;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MovieDbApplication.class)
@WebIntegrationTest
public class DirectorControllerTest {

	private static final String URL_DIRECTOR = "http://localhost:8080/v1/director";

	@Autowired
	private DirectorService directorService;

	private RestTemplate restTemplate = new TestRestTemplate();

	@Test
	public void createDirector() {
		// build request data
		Director director = TestUtil.createDirector("João");

		Director response = restTemplate.postForObject(URL_DIRECTOR, director, Director.class);

		// assert the response
		Assert.assertNotNull(response);

		Director directorFromDb = directorService.findById(response.getIdDirector());
		Assert.assertEquals("João", directorFromDb.getName());
		Assert.assertEquals(director.getBiography(), directorFromDb.getBiography());
		Assert.assertEquals(director.getCountry(), directorFromDb.getCountry());

		// remove object from DB.
		directorService.delete(directorFromDb);
	}

	@Test
	public void deleteDirector() {
		// build request data
		Director director = TestUtil.createDirector("João");

		directorService.save(director);
		Long idDirector = director.getIdDirector();

		// invoke API to delete the resource
		restTemplate.delete(URL_DIRECTOR + "/" + idDirector);

		// try to fetch directly from DB
		Director directorFromDb = directorService.findById(idDirector);

		// assert that there is no data found
		Assert.assertNull(directorFromDb);
	}

	@Test
	public void deleteAllDirectors() {
		// build request data
		Director director = TestUtil.createDirector("João");
		Director director2 = TestUtil.createDirector("Vitor");

		directorService.save(Arrays.asList(director, director2));

		// invoke API to delete the resource
		restTemplate.delete(URL_DIRECTOR);

		// try to fetch directly from DB
		List<Director> directorsFromDb = directorService.findAll();

		// assert that there is no data found
		Assert.assertTrue(directorsFromDb.isEmpty());
	}
	
	@Test
	public void getDirector() {
		// build request data
		Director director = TestUtil.createDirector("João");

		directorService.save(director);

		// invoke API to delete the resource
		Director response = restTemplate.getForObject(URL_DIRECTOR + "/" + director.getIdDirector(), Director.class);

		// assert the response
		Assert.assertNotNull(response);

		Assert.assertEquals(director.getIdDirector(), response.getIdDirector());
		Assert.assertEquals(director.getName(), response.getName());
		Assert.assertEquals(director.getBiography(), response.getBiography());
		Assert.assertEquals(director.getCountry(), response.getCountry());
		Assert.assertEquals(director.getVersion(), response.getVersion());
		
		// remove object from DB.
		directorService.delete(response);
	}
	
	@Test
	public void getAllDirectors() {
		// build request data
		Director director = TestUtil.createDirector("João");
		Director director2 = TestUtil.createDirector("Vitor");

		directorService.save(Arrays.asList(director, director2));

		// invoke API to delete the resource
		List<?> response = restTemplate.getForObject(URL_DIRECTOR, List.class);

		// assert the response
		Assert.assertNotNull(response);

		Assert.assertTrue(response.size() == 2);
		
		// remove object from DB.
		directorService.deleteAll();
	}

}
