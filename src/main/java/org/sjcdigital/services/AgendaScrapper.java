/**
 * 
 */
package org.sjcdigital.services;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.sjcdigital.model.Agenda;
import org.sjcdigital.model.Compromisso;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author pedro-hos@outlook.com
 *
 */
@ApplicationScoped
public class AgendaScrapper {
	
	private static final String SEM_COMPROMISSO_OFICIAL = "Sem compromisso oficial";
	private static final Logger LOGGER = LoggerFactory.getLogger(AgendaScrapper.class);
	
	@ConfigProperty(name = "scrapper.agent")
	String agent;
	
	@ConfigProperty(name = "scrapper.timeout")
	int timeout;
	
	@ConfigProperty(name = "scrapper.agenda.url")
	String agendaUrl;
	
	DateTimeFormatter dataPattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	
	/**
	 * 
	 * @param dia using format yyyy-MM-dd
	 * @return
	 * @throws IOException
	 */
	public Agenda extraiDadosDoDia(final String dia) throws IOException { 
		
		Agenda agenda = new Agenda();
		agenda.dia = LocalDate.parse(dia, dataPattern);
		
		Document page = getDocument(agendaUrl + dia);
		
		if(SEM_COMPROMISSO_OFICIAL.equals(page.select("div.portalMessage").text())) {
			agenda.semCompromisso = true; 
			return agenda;
		}
		
		Elements divCompromissos = page.select("div.item-compromisso");
		
		List<Compromisso> compromissos = new ArrayList<>();
		agenda.compromissos =  compromissos;
		
		divCompromissos.forEach(e -> {
			
			Compromisso compromisso = new Compromisso();
			compromisso.titulo = e.select("h4.compromisso-titulo").text();
			
			if(SEM_COMPROMISSO_OFICIAL.equals(compromisso.titulo)) {
				agenda.semCompromisso = true;
			} else {
			
				compromisso.local = e.select("div.compromisso-local").text();
				compromisso.inicio = convertoToLocalTime(e.select("time.compromisso-inicio").text());
				compromisso.fim = convertoToLocalTime(e.select("time.compromisso-fim").text());
			
				agenda.horasTrabalhadas = agenda.horasTrabalhadas.plus(calculaHoras(compromisso.inicio, compromisso.fim));
			
				compromissos.add(compromisso);
			}
		});
		
		return agenda;
		
	}
	
	/**
	 * @param horasTrabalhadas
	 * @param inicio
	 * @param fim
	 * @return
	 */
	private Duration calculaHoras(LocalTime inicio, LocalTime fim) {
		return Duration.between(inicio, fim);
	}

	/**
	 * 
	 * @param hora
	 * @return
	 */
	private LocalTime convertoToLocalTime(final String hora) {
		
		if("".equals(hora)) {
			LOGGER.info("Hora sem preencher para o dia!");
			return LocalTime.of(0, 10);
		}
		
		String[] horaSplit = hora.split("h");
		return  LocalTime.of(Integer.valueOf(horaSplit[0]), Integer.valueOf(horaSplit[1]));
	}
	
	
	/**
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	private Document getDocument(final String url) throws IOException {
		return getResponse(url).parse();
	}
	
	/**
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	private Response getResponse(final String url) throws IOException {
		
		//System.setProperty("javax.net.ssl.trustStore", "/home/pesilva/workspace/code/pessoal/agenda-presidentes/ssl/gov.br.jks");
		Response response = Jsoup.connect(url)
								.userAgent(agent)
								.timeout(timeout)
								.method(Method.GET)
								.followRedirects(true)
								.execute();
		return response;
	}

}
