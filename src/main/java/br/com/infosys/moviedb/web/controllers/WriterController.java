package br.com.infosys.moviedb.web.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.infosys.moviedb.core.services.WriterService;
import br.com.infosys.moviedb.domain.entities.Writer;

@RestController
@RequestMapping("${api.url.writer}")
public class WriterController {

	private static final Logger logger = LoggerFactory.getLogger(WriterController.class);

	private WriterService writerService;

	@Autowired
	public WriterController(WriterService writerService) {
		this.writerService = writerService;
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Writer> createWriter(@RequestBody Writer writer) {
		logger.info("Creating a new Writer: " + writer.getName());

		if (writerService.exists(writer.getIdWriter())) {
			logger.info("A Writer with id: " + writer.getIdWriter() + " already exist!");
			return new ResponseEntity<Writer>(HttpStatus.CONFLICT);
		}

		Writer persistedWriter = writerService.save(writer);

		return new ResponseEntity<Writer>(persistedWriter, HttpStatus.CREATED);
	}

}
