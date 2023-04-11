/**
 * 
 */
package org.sjcdigital.services;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
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
import org.sjcdigital.utils.ParserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author pedro-hos@outlook.com
 *
 */
@ApplicationScoped
public class AgendaScrapper {
	
	private static final String SEM_COMPROMISSO_OFICIAL = "sem compromisso oficial";
	private static final String SEM_COMPROMISSO_AGENDADO = "atualmente não existem compromissos agendados";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AgendaScrapper.class);
	
	@ConfigProperty(name = "scrapper.agent")
	String agent;
	
	@ConfigProperty(name = "scrapper.timeout")
	int timeout;
	
	@ConfigProperty(name = "scrapper.agenda.url")
	String agendaUrl;
	
	/**
	 * 
	 * @param dia using format yyyy-MM-dd
	 * @return
	 * @throws IOException
	 */
	public Agenda extraiDadosDoDia(final String dia) throws IOException { 
		
		Agenda agenda = new Agenda();
		agenda.dia = ParserUtils.convertToLocalDate(dia);
		
		Document page = getDocument(agendaUrl + dia);
		
		if(SEM_COMPROMISSO_OFICIAL.equals(trataString(page.select("div.portalMessage").text()))) {
			agenda.semCompromisso = true; 
			return agenda;
		}
		
		if(SEM_COMPROMISSO_AGENDADO.equals(trataString(page.select("li.sem-compromisso").text()))) {
			agenda.semCompromisso = true;
			return agenda;
		}
		
		Elements divCompromissos = page.select("div.item-compromisso");
		
		List<Compromisso> compromissos = new ArrayList<>();
		agenda.compromissos =  compromissos;
		
		divCompromissos.forEach(e -> {
			
			Compromisso compromisso = new Compromisso();
			compromisso.titulo = e.select("h2.compromisso-titulo").text();
			
			if(SEM_COMPROMISSO_OFICIAL.equals(trataString(compromisso.titulo))) {
				agenda.semCompromisso = true;
			} else {
			
				compromisso.local = e.select("div.compromisso-local").text();
				String inicioTxt = e.select("time.compromisso-inicio").text();
				String fimTxt = e.select("time.compromisso-fim").text();
				
				compromisso.inicio = convertoToLocalTime(inicioTxt);
				compromisso.fim = convertoToLocalTime(inicioTxt, fimTxt);
			
				agenda.horasTrabalhadas = agenda.horasTrabalhadas.plus(calculaHoras(compromisso.inicio, compromisso.fim));
			
				compromissos.add(compromisso);
			}
		});
		
		return agenda;
		
	}
	
	private static String trataString(String value) {
		return value.replaceAll("\\.", "").toLowerCase();
	}
	
	/**
	 * @param horasTrabalhadas
	 * @param inicio
	 * @param fim
	 * @return
	 */
	private Duration calculaHoras(LocalTime inicio, LocalTime fim) {
		Duration duration = Duration.between(inicio, fim);
		return duration.isNegative() ? duration.negated() : duration;
	}

	/**
	 * 
	 * @param horaInicio
	 * @return
	 * @throws Exception 
	 */
	private LocalTime convertoToLocalTime(final String horaInicio, final String horaFim) {
		
		if("".equals(horaInicio)) {
			LOGGER.error("Hora incio sem preencher para o dia!");
			return null;
		} else if("".equals(horaFim)) {
			LOGGER.warn("Hora fim sem preencher para o dia!");
			String[] horaSplit = horaInicio.split("h");
			return LocalTime.of(Integer.valueOf(horaSplit[0]), Integer.valueOf(horaSplit[1])).plusMinutes(10);
			
		}
		
		String[] horaSplit = horaFim.split("h");
		return LocalTime.of(Integer.valueOf(horaSplit[0]), Integer.valueOf(horaSplit[1]));
		
	}
	
	/**
	 * 
	 * @param horaInicio
	 * @return
	 * @throws Exception 
	 */
	private LocalTime convertoToLocalTime(final String horaInicio) {
		
		if("".equals(horaInicio)) {
			LOGGER.error("Hora sem preencher para o dia!");
			return null;
		}
		
		String[] horaSplit = horaInicio.split("h");
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
