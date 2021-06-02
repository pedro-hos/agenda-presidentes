/**
 * 
 */
package org.sjcdigital.model.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;

import org.sjcdigital.model.constants.Tipos;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

/**
 * @author pedro-hos@outlook.com
 *
 */
@Entity
public class Proposicoes extends PanacheEntity {

	public Integer processo;

	@Column(length = 4)
	public Integer ano;

	@Enumerated(EnumType.ORDINAL)
	public Tipos tipo;

	public String situacao;
	
	@Column(length = 1000)
	public String ementa;
	
	public Integer protocolo;

	@Column(columnDefinition = "DATE")
	public LocalDate data;

	@OneToMany(mappedBy = "proposicao", cascade = CascadeType.ALL)
	public List<Proponentes> proponetes = new ArrayList<>();

	@Override
	public String toString() {

		return "processo: " + processo + " ano: " + ano + " tipo: " + tipo.getText() + " situação: " + situacao
				+ " ementa: " + ementa + " protocolo: " + protocolo + " data: " + data + " proponentes: [ "
				+ proponetesToString() + " ]";
	}

	/**
	 * @return
	 */
	private String proponetesToString() {
		StringBuilder lista = new StringBuilder();
		
		proponetes.forEach(p -> {
			lista.append(p.nome);
			
			if(proponetes.size() > 1) {
				lista.append(", ");
			}
			
		});
		
		return lista.toString();
	}

}
