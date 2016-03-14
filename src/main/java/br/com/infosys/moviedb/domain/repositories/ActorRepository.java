package br.com.infosys.moviedb.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.infosys.moviedb.domain.entities.Actor;

/**
 * Spring Data JPA repository for the Actor entity.
 * 
 * @author vitor191291@gmail.com
 *
 */
@Repository
public interface ActorRepository extends JpaRepository<Actor, Long> {

}
