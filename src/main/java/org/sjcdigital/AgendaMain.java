/**
 * 
 */
package org.sjcdigital;

import java.util.Arrays;

import javax.inject.Inject;

import org.sjcdigital.model.Agenda;
import org.sjcdigital.services.AgendaScrapper;
import org.sjcdigital.services.CSVParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

/**
 * @author pedro-hos@outlook.com
 *
 */
@QuarkusMain
public class AgendaMain implements QuarkusApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(AgendaMain.class);

	@Inject
	AgendaScrapper scrapper;

	@Inject
	CSVParser csvParser;

	@Override
	public int run(String... args) throws Exception {
		
		if (args.length == 0) {

			LOGGER.info("NO parameters found. If you want, use: ./mvnw compile quarkus:dev -Dquarkus.args='yyyy-MM-dd\'");
			Quarkus.waitForExit();

		} else {
			String data = args[0];
			LOGGER.info("Buscando informações: " + data );
			Agenda agenda = scrapper.extraiDadosDoDia(data);
			csvParser.convertAndSaveData(Arrays.asList(agenda));
		}

		return 0;
	}

}
