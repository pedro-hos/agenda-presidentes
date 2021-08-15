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
import org.sjcdigital.utils.ParserUtils;
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
	
	@Inject
	Relatorio summarize;

	private static final Logger LOGGER = LoggerFactory.getLogger(Startup.class);

	public void populate(@Observes StartupEvent ev) {

		LOGGER.info("The application is starting...");
		
		/*
		 * try {
		 * 
		 * LocalDate start = LocalDate.of(2019, 01, 01); List<Agenda> agendas = new
		 * ArrayList<>();
		 * 
		 * while(!LocalDate.now().plusDays(1).isEqual(start)) {
		 * LOGGER.info("Buscando informações: " + start);
		 * agendas.add(scrapper.extraiDadosDoDia(start.format(ParserUtils.
		 * DATA_PATTERN_YYYY_MM_DD))); start = start.plusDays(1); }
		 * 
		 * LOGGER.info("Total entradas >>>> " + agendas.size());
		 * csvParser.convertAndSaveData(agendas);
		 * 
		 * 
		 * } catch (IOException e) { e.printStackTrace(); }
		 */
		
		summarize.geraRelatorio();
		

	}
	
}
