package br.com.infosys.moviedb.core.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.infosys.moviedb.domain.entities.Actor;
import br.com.infosys.moviedb.domain.repositories.ActorRepository;

/**
 * Service class for managing actors.
 * 
 * @author vitor191291@gmail.com
 *
 */
@Service
@Transactional
public class ActorService {

	private static final Logger logger = LoggerFactory.getLogger(ActorService.class);

	private ActorRepository actorRepository;

	@Autowired
	public ActorService(ActorRepository actorRepository) {
		this.actorRepository = actorRepository;
	}

	public Actor save(Actor entity) {
		return actorRepository.saveAndFlush(entity);
	}

	public List<Actor> save(Iterable<Actor> entities) {
		List<Actor> list = actorRepository.save(entities);
		actorRepository.flush();

		return list;
	}

	public void deleteById(Long id) {
		actorRepository.delete(id);
	}

	public void delete(Actor entity) {
		actorRepository.delete(entity);
	}

	public void delete(Iterable<Actor> entities) {
		actorRepository.delete(entities);
	}

	public void deleteAll() {
		actorRepository.deleteAllInBatch();
	}

	@Transactional(readOnly = true)
	public Actor findById(Long id) {
		Actor actor = actorRepository.findOne(id);
		return actor;
	}

	@Transactional(readOnly = true)
	public List<Actor> findAll() {
		List<Actor> actor = actorRepository.findAll();
		return actor;
	}

	@Transactional(readOnly = true)
	public boolean exists(Long id) {
		if (id != null) {
			return actorRepository.exists(id);
		}

		return false;
	}
}
