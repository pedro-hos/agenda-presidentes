package org.sjcdigital.services;

import static org.sjcdigital.utils.ParserUtils.DATA_PATTERN_DD_MM_YYYY;
import static org.sjcdigital.utils.ParserUtils.convertToLocalDate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.sjcdigital.model.AgendaSummarized;
import org.sjcdigital.model.CompromissoSummarized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * @author Pedro Hos <pedro-hos@outlook.com>
 *
 */
@ApplicationScoped
public class Relatorio {
	
	@ConfigProperty(name = "files.path")
	String baseDir;
	
	String fileName = "report.json";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Relatorio.class);
	
	public void geraRelatorio() {
		
		LOGGER.info("Gerando relatório ...");
		
		try {
			
			var registers = Files.walk(Paths.get(baseDir))
					.filter(Files::isRegularFile)
	                .filter(p -> !fileName.equals(p.toFile().getName()))
	                .filter(p -> p.toString().endsWith("csv"))
	                .parallel()
	                .flatMap(Relatorio::readFirstLine)
	                .map(Relatorio :: getDayAndHour)
	                .collect(Collectors.toList());
			
			var total = registers.parallelStream().parallel().reduce(Duration.ZERO, calculaduracao(), Duration::plus);
			
			var agenda = new AgendaSummarized();
			agenda.setHorasTotais(total);
			agenda.setHorasTotaisLista(registers);
			agenda.setHorasAnoLista(summaryzePorAno(registers));
			agenda.setDuracaoText("O Presidente trabalhou aproximadamente " + total.toDays() + " dias (" + total.toHours() + " horas)" + " desde o íncio do mandato.");
			
			String teveAgendaTxt = "Foram " + registers.stream().filter(r -> r.getsemCompromisso().equals("Sim")).count() + " dias sem compromisso, e " + 
											  registers.stream().filter(r -> r.getsemCompromisso().equals("Não")).count() + " dias com compromisso, de um total de " + registers.size() +
											  " desde o início do mandato";
			
			agenda.setTeveAgenda(teveAgendaTxt);
			
			
			createJsonFile(agenda);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void createJsonFile(AgendaSummarized agenda) {
		
		
		try {
			
			String path = baseDir + fileName;
			
			LOGGER.info("Salvando em " + path);
			Files.write(Paths.get(path), new ObjectMapper().writeValueAsString(agenda).getBytes());
			
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private List<CompromissoSummarized> summaryzePorAno(List<CompromissoSummarized> registers) {
		var porAno = new ArrayList<CompromissoSummarized>();
		
		montaAnos().forEach(ano -> {
			
			List<CompromissoSummarized> registrosPorAno = registers.stream().filter(l -> convertToLocalDate(l.getData(), DATA_PATTERN_DD_MM_YYYY).getYear() == ano)
									     			.collect(Collectors.toList());
			
			Duration totalAno = registrosPorAno.stream().reduce(Duration.ZERO, calculaduracao(), Duration::plus);
			
			long diasSemCompromisso = registrosPorAno.stream().filter(r -> r.getsemCompromisso().equals("Sim")).count();
			long diasComCompromisso = registrosPorAno.stream().filter(r -> r.getsemCompromisso().equals("Não")).count();
			
			CompromissoSummarized summarized = new CompromissoSummarized(ano + "", 
																		 totalAno, 
																		 "Não", 
																		 "O Presidente trabalhou aproximadamente: " + totalAno.toDays() + " dias, (" + totalAno.toHours() + " horas)" + " em " + ano,
																		 "Foram " + diasSemCompromisso + " dias sem compromisso, e " 
																		          + diasComCompromisso + " dias com compromisso, de um total de " + registrosPorAno.size() + " em " + 2019);
			porAno.add(summarized);
			
		});
		
		return porAno;
	}
	
	private static List<Integer>  montaAnos( ) {
		
		List<Integer> anos = new LinkedList<>();
		anos.add(2019);
		anos.add(2020);
		anos.add(2021);
		
		Integer anoAtual = LocalDateTime.now().getYear();
		
		if(!anos.contains(anoAtual)) {
			anos.add(anoAtual);
		}
		
		return anos;
	}

	private static BiFunction<Duration, ? super CompromissoSummarized, Duration> calculaduracao() {
		return (first, registro) -> first.plus(registro.getDuracao());
	}
	
	private static CompromissoSummarized getDayAndHour(final String line) {
		var parts = line.replaceAll("\"", "").split(",");
		Duration minutes = Duration.ofMinutes(Long.valueOf(parts[2]));
		return new CompromissoSummarized(parts[0], minutes, parts[1], "Dia com " + Long.valueOf(parts[2]) + " minutos na agenda");
	}
	
	private static Stream<String> readFirstLine(final Path path) {
		
		try {
			
			return Stream.of(Files.lines(path).skip(1).findFirst().get());
			
		} catch (Exception e) {
			System.out.println("Not able to read " + path);
            return Stream.empty();
		}
		
	}

}
