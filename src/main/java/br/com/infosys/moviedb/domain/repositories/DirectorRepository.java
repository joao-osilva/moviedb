package br.com.infosys.moviedb.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.infosys.moviedb.domain.entities.Director;

/**
 * Spring Data JPA repository for the Director entity.
 * 
 * @author vitor191291@gmail.com
 *
 */
@Repository
public interface DirectorRepository extends JpaRepository<Director, Long> {

}
