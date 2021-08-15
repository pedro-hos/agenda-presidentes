package org.sjcdigital.model;

import java.time.Duration;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author pedro-hos@outlook.com
 *
 */
public class CompromissoSummarized {
	
	private String data;
	
	@JsonIgnore
	private Duration duracao;
	
	private String duracaoText;
	private String semCompromisso;
	private String teveAgenda = "";
	
	/**
	 * @param data
	 * @param duracao
	 * @param semCompromisso
	 */
	public CompromissoSummarized(String data, Duration duracao, String semCompromisso, String duracaoText) {
		super();
		this.data = data;
		this.duracao = duracao;
		this.semCompromisso = semCompromisso;
		this.duracaoText = duracaoText;
	}
	
	/**
	 * @param data
	 * @param duracao
	 * @param semCompromisso
	 */
	public CompromissoSummarized(String data, Duration duracao, String semCompromisso, String duracaoText, String teveAgenda) {
		super();
		this.data = data;
		this.duracao = duracao;
		this.semCompromisso = semCompromisso;
		this.duracaoText = duracaoText;
		this.teveAgenda = teveAgenda;
	}
	
	@Deprecated
	public CompromissoSummarized() { }
	
	
	/**
	 * @return the data
	 */
	public String getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(String data) {
		this.data = data;
	}
	/**
	 * @return the duracao
	 */
	public Duration getDuracao() {
		return duracao;
	}
	/**
	 * @param duracao the duracao to set
	 */
	public void setDuracao(Duration duracao) {
		this.duracao = duracao;
	}
	/**
	 * @return the temCompromisso
	 */
	public String getsemCompromisso() {
		return semCompromisso;
	}
	/**
	 * @param temCompromisso the temCompromisso to set
	 */
	public void setSemCompromisso(String semCompromisso) {
		this.semCompromisso = semCompromisso;
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
