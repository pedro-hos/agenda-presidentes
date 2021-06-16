/**
 * 
 */
package org.sjcdigital.services;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.sjcdigital.model.Agenda;
import org.sjcdigital.model.Compromisso;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opencsv.CSVWriter;

/**
 * @author pedro-hos@outlook.com
 *
 */
@ApplicationScoped
public class CSVParser {
	
	@ConfigProperty(name = "files.path")
	String path;
	 
	static final String TEXT_ENCLOSER = "\"";
	static final Logger LOGGER = LoggerFactory.getLogger(CSVParser.class);
	static final String SEPARATOR = ",";
	static final String[] HEADER =  { "DIA", 
			 						   "SEM_COMPROMISSO", 
			 						   "DURACAO_DIA", 
			 						   "INICIO", 
			 						   "FIM",
			 						   "DURACAO_COMPROMISSO",
			 						   "TITULO", 
			 						   "LOCAL" };
	 
	 public void convertAndSaveData(final List<Agenda> agendas) throws IOException {
		 
		 LOGGER.info("Convertendo e Salvando CSV em " + path);
		 
		 for (Agenda agenda : agendas) {
			 
			 LOGGER.info("Escrevendo ..." + agenda.dia);
			 
			 String filePath = createDirectoryIfDoesntExists(montaPath(agenda.dia)) + agenda.dia.getDayOfMonth() + ".csv";
			 
			 Writer writer = Files.newBufferedWriter(Paths.get(filePath));
			 CSVWriter csvWriter = new CSVWriter(writer,
	                    CSVWriter.DEFAULT_SEPARATOR,
	                    CSVWriter.DEFAULT_QUOTE_CHARACTER,
	                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
	                    CSVWriter.DEFAULT_LINE_END);
			 
			 csvWriter.writeNext(HEADER);
			 
			 if(agenda.semCompromisso) {
				 
				 csvWriter.writeNext(new String[] {  formataData(agenda.dia), 
						 							 converteBoolean(agenda.semCompromisso), 
						 							 String.valueOf(agenda.horasTrabalhadas.toMinutes()) });
				 
				 
			 } else {
				 
				 for(Compromisso compromisso : agenda.compromissos) {
					 
					 csvWriter.writeNext(new String[] {  formataData(agenda.dia), 
							 							 converteBoolean(agenda.semCompromisso), 
							 							 String.valueOf(agenda.horasTrabalhadas.toMinutes()),
							 							 compromisso.inicio.toString(),
							 							 compromisso.fim.toString(),
							 							 String.valueOf(Duration.between(compromisso.inicio, compromisso.fim).toMinutes()),
							 							 escapeField(compromisso.titulo),
							 							 escapeField(compromisso.local) });
					 
					 
				 }
				 
			 }
			 
			 csvWriter.close();
			
		}
		 
	 }
	 
	 
	 protected String escapeField(Object v) {
			if (v instanceof CharSequence) {
				String escaped = ((String) v).replaceAll(TEXT_ENCLOSER, "\\\\" + TEXT_ENCLOSER + "");
				return TEXT_ENCLOSER + escaped + TEXT_ENCLOSER;
			}
			return v.toString();
		}
	 

	private String formataData(final LocalDate data) {
		return data.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
	}

	private String montaPath(final LocalDate dia) {
		return path + dia.getYear() + "/" + dia.getMonthValue() + "/";
	}
	 
	 private String converteBoolean(final boolean value) {
		 return value ? "Sim" : "Não";
	 }
	 
	 private String createDirectoryIfDoesntExists(String directoryName) {

	        var directory = new File(directoryName);
	        
	        if (!directory.exists()) {
	            directory.mkdirs();
	        }
	        
	        return directoryName;
	    }

}
