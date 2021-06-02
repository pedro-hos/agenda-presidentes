/**
 * 
 */
package org.sjcdigital.services;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.sjcdigital.model.repositories.ProposicoesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.runtime.StartupEvent;

/**
 * @author pedro-hos@outlook.com
 *
 */
@ApplicationScoped
public class PopulaDBService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PopulaDBService.class);
	
	@Inject
	ProposicoesBot bot;
	
	@Inject
	ProposicoesRepository proposicoesRepository;
	
	@ConfigProperty(name = "location.files")
	String path;
	
	@ConfigProperty(name = "start.csv.data")
	String filename;
	
	/**
	 * This method will populate the database during the startup if is empty.
	 * @param ev
	 */
	
	public void populate(@Observes StartupEvent ev) {
		
		LOGGER.info("The application is starting...");
		
		if(proposicoesRepository.count() == 0) {
			//bot.extractDataFromCSV(path + filename);
			bot.buscaProposicoesCSV();
			LOGGER.info(">>>> Total de proposições salvas: " + proposicoesRepository.count());
		}
		
	}

}
