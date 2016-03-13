package br.com.infosys.moviedb.core.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.infosys.moviedb.domain.entities.Actor;
import br.com.infosys.moviedb.domain.repositories.ActorRepository;

@Service
public class ActorService {

	private static final Logger logger = LoggerFactory.getLogger(ActorService.class);

	private ActorRepository actorRepository;

	@Autowired
	public ActorService(ActorRepository actorRepository) {
		this.actorRepository = actorRepository;
	}

	@Transactional
	public Actor save(Actor entity) {
		return actorRepository.saveAndFlush(entity);
	}
	
	@Transactional
	public List<Actor> save(Iterable<Actor> entities) {
		List<Actor> list = actorRepository.save(entities);
		actorRepository.flush();
		
		return list;
	}
	
	@Transactional
	public void deleteById(Long id) {
		actorRepository.delete(id);
	}

	@Transactional
	public void delete(Actor entity) {
		actorRepository.delete(entity);
	}
	
	@Transactional
	public void delete(Iterable<Actor> entities) {
		actorRepository.delete(entities);
	}
	
	@Transactional
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
