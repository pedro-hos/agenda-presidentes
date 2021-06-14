/**
 * 
 */
package org.sjcdigital.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

/**
 * @author pedro-hos@outlook.com
 *
 */
@Entity
public class Proponentes extends PanacheEntity {
	
	@Deprecated
	public Proponentes() { }
	
	public Proponentes(String nome) {
		super();
		this.nome = nome;
	}

	@Column(unique = true)
	public String nome;
	
	@ManyToOne(fetch = FetchType.LAZY)
	public Proposicoes proposicao;

}
