/**
 * 
 */
package org.sjcdigital.model.repositories;

import javax.enterprise.context.ApplicationScoped;

import org.sjcdigital.model.entity.Proposicoes;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

/**
 * @author pedro-hos@outlook.com
 *
 */
@ApplicationScoped
public class ProposicoesRepository implements PanacheRepository<Proposicoes> {

}
