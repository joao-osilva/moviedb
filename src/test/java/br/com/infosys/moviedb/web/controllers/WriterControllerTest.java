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
import br.com.infosys.moviedb.core.services.WriterService;
import br.com.infosys.moviedb.domain.entities.Writer;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MovieDbApplication.class)
@WebIntegrationTest
public class WriterControllerTest {

	private static final String URL_WRITER = "http://localhost:8080/v1/writer";

	@Autowired
	private WriterService writerService;

	private RestTemplate restTemplate = new TestRestTemplate();

	@Test
	public void createWriter() {
		// build request data
		Writer writer = TestUtil.createWriter("Marco");

		Writer response = restTemplate.postForObject(URL_WRITER, writer, Writer.class);

		// assert the response
		Assert.assertNotNull(response);

		Writer writerFromDb = writerService.findById(response.getIdWriter());
		Assert.assertEquals("Marco", writerFromDb.getName());
		Assert.assertEquals(writer.getBiography(), writerFromDb.getBiography());
		Assert.assertEquals(writer.getCountry(), writerFromDb.getCountry());

		// remove object from DB.
		writerService.delete(writerFromDb);
	}

	@Test
	public void deleteWriter() {
		// build request data
		Writer writer = TestUtil.createWriter("João");

		writerService.save(writer);
		Long idWriter = writer.getIdWriter();

		// invoke API to delete the resource
		restTemplate.delete(URL_WRITER + "/" + idWriter);

		// try to fetch directly from DB
		Writer writerFromDb = writerService.findById(idWriter);

		// assert that there is no data found
		Assert.assertNull(writerFromDb);
	}

	@Test
	public void deleteAllWriters() {
		// build request data
		Writer writer = TestUtil.createWriter("João");
		Writer writer2 = TestUtil.createWriter("Vitor");

		writerService.save(Arrays.asList(writer, writer2));

		// invoke API to delete the resource
		restTemplate.delete(URL_WRITER);

		// try to fetch directly from DB
		List<Writer> writersFromDb = writerService.findAll();

		// assert that there is no data found
		Assert.assertTrue(writersFromDb.isEmpty());
		;
	}
}
