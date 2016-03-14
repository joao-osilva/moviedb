package br.com.infosys.moviedb.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.infosys.moviedb.domain.entities.Movie;


/**
 * Spring Data JPA repository for the Movie entity.
 * 
 * @author vitor191291@gmail.com
 *
 */
@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

}
