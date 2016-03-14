package br.com.infosys.moviedb.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.infosys.moviedb.domain.entities.Writer;

/**
 * Spring Data JPA repository for the Writer entity.
 * 
 * @author vitor191291@gmail.com
 *
 */
@Repository
public interface WriterRepository extends JpaRepository<Writer, Long> {

}
