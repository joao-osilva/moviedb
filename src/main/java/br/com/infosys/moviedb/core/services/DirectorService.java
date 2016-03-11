package br.com.infosys.moviedb.core.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.infosys.moviedb.domain.entities.Director;
import br.com.infosys.moviedb.domain.repositories.DirectorRepository;

@Service
public class DirectorService {

	private static final Logger logger = LoggerFactory.getLogger(DirectorService.class);

	private DirectorRepository directorRepository;

	@Autowired
	public DirectorService(DirectorRepository directorRepository) {
		this.directorRepository = directorRepository;
	}
	
	@Transactional
	public Director save(Director entity) {
		return directorRepository.saveAndFlush(entity);
	}

	@Transactional
	public void delete(Director entity) {
		directorRepository.delete(entity);
	}

	@Transactional(readOnly = true)
	public Director findById(Long id) {
		Director director = directorRepository.findOne(id);
		return director;
	}

	@Transactional(readOnly = true)
	public List<Director> findAll() {
		List<Director> director = directorRepository.findAll();
		return director;
	}

	@Transactional(readOnly = true)
	public boolean exists(Long id) {
		return directorRepository.exists(id);
	}

}
