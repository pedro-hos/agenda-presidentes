/**
 * 
 */
package org.sjcdigital;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.inject.Inject;

import org.sjcdigital.model.Agenda;
import org.sjcdigital.services.AgendaScrapper;
import org.sjcdigital.services.CSVParser;
import org.sjcdigital.services.JoinAllData;
import org.sjcdigital.services.Relatorio;
import org.sjcdigital.utils.ParserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

/**
 * @author pedro-hos@outlook.com
 *
 */
@Command(name = "Agenda dos Presidentes", mixinStandardHelpOptions = true)
public class AgendaMain implements Runnable {

	private static final Logger LOGGER = LoggerFactory.getLogger(AgendaMain.class);

	@Inject
	AgendaScrapper scrapper;

	@Inject
	CSVParser csvParser;
	
	@Inject
	Relatorio relatorio;
	
	@Inject
	JoinAllData joinData;
	
	@Option(names = {"-d", "--dia"}, split = "-", required = true, description = "Obrigatório. Use o parâmetro -d ou --dia, para indicar os dias da busca. Separe por '-' para indicar range. Ex. -d 1, -d 1-10, --dia 5-20")
	Integer[] dias;
	
	@Option(names = {"-m", "--mes"}, split = "-", required = true, description = "Obrigatório. Use o parâmetro -m ou --mes, para indicar os meses da busca. Separe por '-' para indicar range. Ex. -m 1, -d 1-10, --dia 1-12")
	Integer[] meses;
	
	@Option(names = {"-a", "--ano"}, split = "-", required = true, description = "Obrigatório. Use o parâmetro -a ou --ano, para indicar os anos da busca. O ano começa em 2019. Separe por '-' para indicar range. Ex. -d 2019, -d 2020, --dia 2019-2021")
	Integer[] anos;
	
	@Option(names = {"-r", "--relatorio"}, required = false, defaultValue = "false", description = "Opcional. Caso queira gerar o relatório json")
	boolean report;
	
	@Option(names = {"-j", "--join"}, required = false, defaultValue = "false", description = "Opcional. Caso queira gerar o juntar todos em um unico CSV")
	boolean join;

	@Override
	public void run() {
		
		List<Integer> anosBusca  = montaRange(anos);
		List<Integer> mesesBusca = montaRange(meses);
		List<Integer> diasBusca  = montaRange(dias);
		
		anosBusca.forEach(ano -> {
			mesesBusca.forEach(mes -> {
				diasBusca.forEach(dia -> {
					
					try {
						
						var data = LocalDate.of(ano, mes, dia).format(ParserUtils.DATA_PATTERN_YYYY_MM_DD);
						LOGGER.info("Buscando informações: " + data );
						Agenda agenda = scrapper.extraiDadosDoDia(data);
						csvParser.convertAndSaveData(Arrays.asList(agenda));
						
					} catch (IOException e) {
						e.printStackTrace();
					}
				});
			});
		});
		
		if(report) {
			relatorio.geraRelatorio();
		} 
		
		if(join) {
			joinData.join();
		}
		
	}
	
	private List<Integer> montaRange(Integer[] values) {
		
		if(values.length == 1) {
			return Arrays.asList(values[0]);
		} else {
			return IntStream.rangeClosed(values[0], values[1]).boxed().collect(Collectors.toList());
		}
		
	}

}
