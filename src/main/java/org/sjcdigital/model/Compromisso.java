/**
 * 
 */
package org.sjcdigital.model;

import java.time.LocalTime;

/**
 * @author pedro-hos@outlook.com
 *
 */
public class Compromisso {
	
	public LocalTime inicio;
	public LocalTime fim;
	public String titulo;
	public String local;
	
	@Override
	public String toString() {
		return "Titulo: " + titulo + " Local: " + local +  " Incio: " + inicio + " Fim: " + fim;
	}

}
