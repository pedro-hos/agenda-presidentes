/**
 * 
 */
package org.sjcdigital.services;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

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
	
	public static void main(String[] args) {
		//-d 1-10 -m 8 -a 2021
		
		List<Integer> collect = IntStream.rangeClosed(1, 10).boxed().collect(Collectors.toList());
		collect.forEach(System.out::println);
		
		System.out.println("1".split("-"));
	}

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
