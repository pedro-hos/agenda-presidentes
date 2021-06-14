/**
 * 
 */
package org.sjcdigital.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.sjcdigital.model.constants.Tipos;
import org.sjcdigital.model.entity.Proponentes;
import org.sjcdigital.utils.ParserUtils;
import org.sjcdigital.utils.ScrapperUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author pedro-hos@outlook.com
 *
 */
@ApplicationScoped
public class ProponenteService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProponenteService.class);
	
	@ConfigProperty(name = "url.camara")
	String url;
	
	@ConfigProperty(name = "query.param.busca")
	String queryBuscaAvancada;
	
	@ConfigProperty(name = "query.param.tipo")
	String queryTipo;
	
	@ConfigProperty(name = "query.param.processo")
	String queryProcesso;
	
	@ConfigProperty(name = "query.param.ano")
	String queryAno;
	
	@Inject
	ScrapperUtils scrapperUtils;
	
	/**
	 * @param processo
	 * @param ano
	 * @param tipo
	 * @return
	 */
	public List<Proponentes> buscaProponentesPagina(Integer processo, Integer ano, Tipos tipo) {
		
		Objects.requireNonNull(processo);
		Objects.requireNonNull(ano);
		Objects.requireNonNull(tipo);
		
		List<Proponentes> proponentes = new ArrayList<>();
		
		try {
			
			Document doc = scrapperUtils.getDocument(montaURL(processo, ano, tipo));
			
			Elements next = doc.select("span:contains(Autor(es) da Proposição:)").next("span");
			Elements links = next.select("a[href]");
			
			if(ParserUtils.empty(links.toString())) { 
				proponentes.add(new Proponentes(next.text()));
			} else {
				links.forEach(l -> proponentes.add(new Proponentes(l.text())));
			}
			
		} catch (IOException e) {
			LOGGER.error(String.format("Erro ao buscar propenentes do processo: %d, ano %d e tipo $s", processo, ano, tipo.getText()));
			e.printStackTrace();
		}
		
		return proponentes;
	}
	

	/**
	 * @return
	 */
	private String montaURL(Integer processo, Integer ano, Tipos tipo) {
		return url + queryBuscaAvancada + queryTipo + tipo.getCode() + queryProcesso + processo + queryAno + ano;
	}
	

}
