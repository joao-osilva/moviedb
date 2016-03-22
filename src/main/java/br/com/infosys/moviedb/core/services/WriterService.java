package br.com.infosys.moviedb.core.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.infosys.moviedb.domain.entities.Writer;
import br.com.infosys.moviedb.domain.repositories.WriterRepository;

/**
 * Service class for managing writers.
 * 
 * @author vitor191291@gmail.com
 *
 */
@Service
@Transactional
public class WriterService {

	private static final Logger logger = LoggerFactory.getLogger(WriterService.class);

	private WriterRepository writerRepository;

	@Autowired
	public WriterService(WriterRepository writerRepository) {
		this.writerRepository = writerRepository;
	}

	public Writer save(Writer entity) {
		return writerRepository.saveAndFlush(entity);
	}

	public List<Writer> save(Iterable<Writer> entities) {
		List<Writer> list = writerRepository.save(entities);
		writerRepository.flush();

		return list;
	}
	
	public Writer update(Long id, Writer toBe) {
		Writer asIs = writerRepository.findOne(id);
		
		asIs.setName(toBe.getName());
		asIs.setBiography(toBe.getBiography());
		asIs.setCountry(toBe.getCountry());
		
		writerRepository.flush();
		
		return asIs;
	}

	public void deleteById(Long id) {
		writerRepository.delete(id);
	}

	public void delete(Writer entity) {
		writerRepository.delete(entity);
	}

	public void delete(Iterable<Writer> entities) {
		writerRepository.delete(entities);
	}

	public void deleteAll() {
		writerRepository.deleteAllInBatch();
	}

	@Transactional(readOnly = true)
	public Writer findById(Long id) {
		Writer writer = writerRepository.findOne(id);
		return writer;
	}

	@Transactional(readOnly = true)
	public List<Writer> findAll() {
		List<Writer> writer = writerRepository.findAll();
		return writer;
	}

	@Transactional(readOnly = true)
	public boolean exists(Long id) {
		if (id != null) {
			return writerRepository.exists(id);
		}

		return false;
	}
}
