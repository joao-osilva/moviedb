package br.com.infosys.moviedb.core.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.infosys.moviedb.domain.entities.Writer;
import br.com.infosys.moviedb.domain.repositories.WriterRepository;

@Service
public class WriterService {

	private static final Logger logger = LoggerFactory.getLogger(WriterService.class);

	private WriterRepository writerRepository;

	@Autowired
	public WriterService(WriterRepository writerRepository) {
		this.writerRepository = writerRepository;
	}
	
	@Transactional
	public Writer save(Writer entity) {
		return writerRepository.saveAndFlush(entity);
	}
	
	@Transactional
	public List<Writer> save(Iterable<Writer> entities) {
		List<Writer> list = writerRepository.save(entities);
		writerRepository.flush();
		
		return list;
	}

	@Transactional
	public void deleteById(Long id) {
		writerRepository.delete(id);
	}
	
	@Transactional
	public void delete(Writer entity) {
		writerRepository.delete(entity);
	}
	
	@Transactional
	public void delete(Iterable<Writer> entities) {
		writerRepository.delete(entities);
	}
	
	@Transactional
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
