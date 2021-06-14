/**
 * 
 */
package org.sjcdigital.model.repositories;

import javax.enterprise.context.ApplicationScoped;

import org.sjcdigital.model.entity.Proponentes;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

/**
 * @author pedro-hos@outlook.com
 *
 */
@ApplicationScoped
public class ProponenteRepository implements PanacheRepository<Proponentes> {

	public Proponentes findByNome(final String nome) {
		return find("nome", nome).firstResult();
	}
}
