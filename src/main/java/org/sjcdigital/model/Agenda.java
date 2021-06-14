/**
 * 
 */
package org.sjcdigital.model;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

/**
 * @author pedro-hos@outlook.com
 *
 */
public class Agenda {
	
	public LocalDate dia; 
	public boolean semCompromisso;
	public List<Compromisso> compromissos;
	public Duration horasTrabalhadas = Duration.ZERO;
	
	
}
