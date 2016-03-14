package br.com.infosys.moviedb.web.controllers;

import java.text.ParseException;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;

import br.com.infosys.moviedb.domain.entities.Actor;
import br.com.infosys.moviedb.domain.entities.Director;
import br.com.infosys.moviedb.domain.entities.Movie;
import br.com.infosys.moviedb.domain.entities.Writer;
import br.com.infosys.moviedb.domain.enums.GenreEnum;

public class TestUtil {

	public static Actor createActor(String name) {
		Actor actor = new Actor();
		actor.setName(name);
		actor.setBiography("This is a brief test biography.");
		actor.setCountry("United States");
		
		return actor;
	}

	public static Director createDirector(String name) {
		Director director = new Director();
		director.setName(name);
		director.setBiography("This is a brief test biography.");
		director.setCountry("Denmark");
		
		return director;
	}

	public static Writer createWriter(String name) {
		Writer writer = new Writer();
		writer.setName(name);
		writer.setBiography("This is a brief test biography.");
		writer.setCountry("Germany");
		
		return writer;
	}

	public static Movie createMovie(String title, Director director, List<Writer> writers, List<Actor> cast) throws ParseException {
		Movie movie = new Movie();
		movie.setTitle(title);
		movie.setDirector(director);
		movie.setWriters(new HashSet<>(writers));
		movie.setCast(new HashSet<>(cast));
		movie.setGenre(GenreEnum.Drama);
		movie.setPlotSummary("A brief plot summary of TestMovie, a film by Someone.");
		movie.setCountry("Russia");
		movie.setLanguage("Russian");
		movie.setReleaseDate(DateUtils.parseDate("1977/03/16", "yyyy/MM/dd"));
		
		return movie;
	}

}
