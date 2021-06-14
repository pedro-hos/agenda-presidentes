/**
 * 
 */
package org.sjcdigital.services;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.sjcdigital.model.Agenda;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.runtime.StartupEvent;

/**
 * @author pedro-hos@outlook.com
 *
 */
@ApplicationScoped
public class Startup {
	
	@Inject
	AgendaScrapper scrapper;
	
	@Inject
	CSVParser csvParser;

	private static final Logger LOGGER = LoggerFactory.getLogger(Startup.class);

	public void populate(@Observes StartupEvent ev) {

		LOGGER.info("The application is starting...");
		
		
		try {
			
			//LocalDate start = LocalDate.of(2019, 01, 1);
			List<Agenda> agendas = new ArrayList<>();
			
			//while(!LocalDate.now().isEqual(start)) {
				LOGGER.info("Buscando informações: " + LocalDate.now());
				agendas.add(scrapper.extradaDataFrom(LocalDate.now().format(scrapper.dataPattern)));
				//start = start.plusDays(1);
			//}
			
			LOGGER.info("Total entradas >>>> " + agendas.size());
			csvParser.convertAndSaveData(agendas);
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
}
