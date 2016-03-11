package com.apple.braziltmt;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.web.WebAppConfiguration;

import br.com.infosys.moviedb.MovieDbApplication;
import br.com.infosys.moviedb.core.services.ActorService;
import br.com.infosys.moviedb.domain.entities.Actor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MovieDbApplication.class)
@WebAppConfiguration
public class BraziltmtApplicationTests {

	@Autowired
	ActorService actorService;
	
	@Test
	public void contextLoads() {
		Actor a = new Actor();
		a.setName("Joao");
		a.setBiography("test bio");
		a.setCountry("Brazil");
		
		Actor save = actorService.save(a);
		
		Assert.assertNotNull(save);
		
		System.out.println(save);
		
	}

}
