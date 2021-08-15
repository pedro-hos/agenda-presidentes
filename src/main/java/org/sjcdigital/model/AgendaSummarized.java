package org.sjcdigital.model;

import java.time.Duration;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author pedro-hos@outlook.com
 *
 */
public class AgendaSummarized {

	@JsonSerialize()
	private List<CompromissoSummarized> horasTotaisLista;
	
	@JsonSerialize()
	private List<CompromissoSummarized> horasAnoLista	;
	
	private String duracaoText;
	
	private String teveAgenda;
	
	@JsonIgnore
	private Duration horasTotais;

	/**
	 * @param horasTotaisLista
	 * @param horasAnoLista
	 * @param horasTotais
	 */
	public AgendaSummarized(List<CompromissoSummarized> horasTotaisLista, List<CompromissoSummarized> horasAnoLista, Duration horasTotais) {
		super();
		this.horasTotaisLista = horasTotaisLista;
		this.horasAnoLista = horasAnoLista;
		this.horasTotais = horasTotais;
	}

	public AgendaSummarized() {}
	
	/**
	 * @return the horasTotaisLista
	 */
	public List<CompromissoSummarized> getHorasTotaisLista() {
		return horasTotaisLista;
	}

	/**
	 * @param horasTotaisLista the horasTotaisLista to set
	 */
	public void setHorasTotaisLista(List<CompromissoSummarized> horasTotaisLista) {
		this.horasTotaisLista = horasTotaisLista;
	}

	/**
	 * @return the horasAnoLista
	 */
	public List<CompromissoSummarized> getHorasAnoLista() {
		return horasAnoLista;
	}

	/**
	 * @param horasAnoLista the horasAnoLista to set
	 */
	public void setHorasAnoLista(List<CompromissoSummarized> horasAnoLista) {
		this.horasAnoLista = horasAnoLista;
	}

	/**
	 * @return the horasTotais
	 */
	public Duration getHorasTotais() {
		return horasTotais;
	}

	/**
	 * @param horasTotais the horasTotais to set
	 */
	public void setHorasTotais(Duration horasTotais) {
		this.horasTotais = horasTotais;
	}

	/**
	 * @return the duracaoText
	 */
	public String getDuracaoText() {
		return duracaoText;
	}

	/**
	 * @param duracaoText the duracaoText to set
	 */
	public void setDuracaoText(String duracaoText) {
		this.duracaoText = duracaoText;
	}

	/**
	 * @return the teveAgenda
	 */
	public String getTeveAgenda() {
		return teveAgenda;
	}

	/**
	 * @param teveAgenda the teveAgenda to set
	 */
	public void setTeveAgenda(String teveAgenda) {
		this.teveAgenda = teveAgenda;
	}

}
