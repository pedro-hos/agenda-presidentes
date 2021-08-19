package org.sjcdigital.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Pedro Hos
 *
 */
@ApplicationScoped
public class JoinAllData {
	
	@ConfigProperty(name = "files.path")
	String baseDir;
	
	private static final String SUMMARY_CSV = "summary.csv";
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Relatorio.class);

	
	public void join() {
		
		try {
		
			String outputDir = baseDir + SUMMARY_CSV;
			LOGGER.info("Juntando arquivos para serem salvos em: " + outputDir);
		
			var registers = Files.walk(Paths.get(baseDir))
		            .filter(Files::isRegularFile)
		            .filter(p -> !SUMMARY_CSV.equals(p.toFile().getName()))
		            .filter(p -> p.toString().endsWith("csv"))
		            .parallel()
		            .flatMap(JoinAllData::lines)
		            .collect(Collectors.toList());
			
			StringBuffer body = new StringBuffer(String.join(",", CSVParser.HEADER));
			body.append("\n");
			body.append(registers.stream().collect(Collectors.joining("\n")));
			
			Files.writeString(Paths.get(outputDir), body.toString());
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

	private static Stream<String> lines(Path path) {
		try {
			return Files.lines(path).skip(1);
		} catch (IOException e) {
			System.out.println("Not able to read " + path);
			return Stream.empty();
		}
	}

}
