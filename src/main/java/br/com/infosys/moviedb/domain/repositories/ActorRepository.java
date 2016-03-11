package br.com.infosys.moviedb.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.infosys.moviedb.domain.entities.Actor;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Long> {

}
