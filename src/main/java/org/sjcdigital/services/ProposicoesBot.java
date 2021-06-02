/**
 * 
 */
package org.sjcdigital.services;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.sjcdigital.model.constants.Tipos;
import org.sjcdigital.model.entity.Proponentes;
import org.sjcdigital.model.entity.Proposicoes;
import org.sjcdigital.model.repositories.ProponenteRepository;
import org.sjcdigital.model.repositories.ProposicoesRepository;
import org.sjcdigital.utils.ParserUtils;
import org.sjcdigital.utils.ScrapperUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import com.opencsv.exceptions.CsvValidationException;

/**
 * @author pedro-hos@outlook.com
 *
 */

@ApplicationScoped
public class ProposicoesBot {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProposicoesBot.class);
	
	@ConfigProperty(name = "url.camara")
	String url;
	
	@ConfigProperty(name = "query.param.busca")
	String queryBuscaAvancada;
	
	@ConfigProperty(name = "query.param.inicio")
	String queryInicio;
	
	@ConfigProperty(name = "query.param.final")
	String queryFinal;
	
	@ConfigProperty(name = "form.param.viewstate")
	String viewStateParam;
	
	@ConfigProperty(name = "form.param.eventvalidation")
	String eventValidationParam;
	
	@ConfigProperty(name = "form.param.csv")
	String csvParam;
	
	@ConfigProperty(name = "form.param.csv.value")
	String csvParamValue;
	
	@ConfigProperty(name = "location.files")
	String path;
	
	@Inject
	ProponenteService proponenteService;
	
	@Inject
	ProponenteRepository proponenteRepository;
	
	@Inject
	ProposicoesRepository proposicoesRepository;
	
	@Inject
	ScrapperUtils scrapperUtils;
	
	/**
	 * Isso será um cron para pegar todo inicio de mes o do mes anterio.
	 */
	public void buscaProposicoesCSV() {
		
		try {
			
			Map<String, Map<String, String>> parameters = montaParameters();
			String body = scrapperUtils.getCSVResponse(montaURLMesAnterior(), 
														  parameters.get("formParameters"), 
														  parameters.get("cookies"));
			
			
			
			Files.write(Paths.get(path + LocalDate.now().minusMonths(1) + ".csv"), body.trim().getBytes());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private Map<String, Map<String, String>> montaParameters() throws IOException {
		
		Map<String, Map<String, String>> paramenters = new HashMap<String, Map<String, String>>();
		Map<String, String> values = new HashMap<String, String>();
		
		Response response = scrapperUtils.getResponse(montaURLMesAnterior());
		paramenters.put("cookies", response.cookies());
		paramenters.put("formParameters", values);
		
		Document doc = response.parse();
		values.put(csvParam, csvParamValue);
		values.put(viewStateParam, doc.getElementById(viewStateParam).val());
		values.put(eventValidationParam, doc.getElementById(eventValidationParam).val());
		
		return paramenters;
	}
	
	private String montaURLMesAnterior() {
		
		DateTimeFormatter pattern = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate initial = LocalDate.now().minusMonths(1);
		String start = initial.withDayOfMonth(1).format(pattern);
		String end = initial.withDayOfMonth(initial.lengthOfMonth()).format(pattern);
		
		return url + queryBuscaAvancada + queryInicio + start + queryFinal + end;
		
	}
	
	@Transactional
	public void extractDataFromCSV(final String file) {
		
		long currentLine = 0;
		
		try { 
			
			LOGGER.info("Iniciando extração do CSV ...");
			
			FileReader fileReader = new FileReader(file, StandardCharsets.ISO_8859_1);
			CSVReader reader = new CSVReaderBuilder(fileReader).withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_SEPARATORS)
															   .withSkipLines(1)
															   .build();
			
			String[] lineInArray;
			
			while ((lineInArray = reader.readNext()) != null) {
				
				
				currentLine = reader.getLinesRead();
				
				Proposicoes prop = new Proposicoes();
				
				prop.processo = ParserUtils.convertToInteger(lineInArray[0]);
				prop.ano = Integer.valueOf(lineInArray[1]);
				prop.tipo = Tipos.findByText(lineInArray[2]).orElseThrow();
				prop.situacao = lineInArray[3];
				prop.ementa = lineInArray[4];
				prop.protocolo = ParserUtils.convertToInteger(lineInArray[5]);
				prop.data = ParserUtils.convertToLocalDate(lineInArray[6]);
				
				prop.proponetes = saveOrGetProponent(proponenteService.buscaProponentesPagina(prop.processo, prop.ano, prop.tipo));
				
				proposicoesRepository.persist(prop);
				
				LOGGER.info(prop.toString());
				
			}
			
		} catch (IOException | CsvValidationException e) {
			LOGGER.error("Erro ao ler linha: " + currentLine + " do CSV " + file);
			e.printStackTrace();
		}
		
		
	}

	/**
	 * @param buscaProponentesPagina
	 * @return
	 */
	@Transactional
	private List<Proponentes> saveOrGetProponent(List<Proponentes> buscaProponentesPagina) {
		
		List<Proponentes> nova = new ArrayList<>();
		
		for (Proponentes proponentes : buscaProponentesPagina) {
			
			Proponentes findByNome = proponenteRepository.findByNome(proponentes.nome);
			
			if(findByNome != null) {
				nova.add(findByNome);
			} else {
				proponenteRepository.persist(proponentes);
				nova.add(proponentes);
			}
			
		}
		
		return nova;
	}
	
}
