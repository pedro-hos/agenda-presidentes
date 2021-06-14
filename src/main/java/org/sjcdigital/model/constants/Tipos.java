/**
 * 
 */
package org.sjcdigital.model.constants;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author pedro-hos@outlook.com
 * Os códigos são provenientes do site da câmara.
 *
 */
public enum Tipos {
	
	INDICACAO(319, "Indicação"),
	MOCAO(341,"Moção"),
	REQUERIMENTO(340,"Requerimento"),
	DOCUMENTOS_DIVERSOS(371,"Documentos Diversos"),
	ATA_SESSAO_EXTRAORDINARIA(401,"Ata Sessão Extraordinária"),
	ATA_SESSAO_ORDINARIA(397,"Ata Sessão Ordinária"),
	ATA_SESSAO_SOLENE(400,"Ata Sessão Solene"),
	EMENDA(347,"Emenda"),
	EMENDA_SUBSTITUTIVO(378,"Emenda ao Substitutivo"),
	MANIFESTACAO_AD_HOC(392,"Manifestação AD HOC"),
	PARECER_COMISSAO_CONJ_JUSTICA_E_ECONOMIA(405,"Parecer da Comissão Conjunta de Justiça e Economia - Rito Prioritário "),
	PARECER_COMISSAO_CULTURA_ESPORTE(343,"Parecer da Comissão de Cultura e Esportes"),
	PARECER_COMISSAO_ECON_FIN_ORCAMENTO(353,"Parecer da Comissão de Economia, Finanças e Orçamento"),
	PARECER_COMISSAO_EDU_PROMOCAO_SOCIAL(357,"Parecer da Comissão de Educação e Promoção Social"),
	PARECER_COMISSAO_ETICA(355,"Parecer da Comissão de Ética"),
	PARECER_COMISSAO_JUST_REDACAO_DIR_HUMANOS(350,"Parecer da Comissão de Justiça, Redação e Direitos Humanos"),
	PARECER_COMISSAO_MEIO_AMBIENTE(356,"Parecer da Comissão de Meio Ambiente"),
	PARECER_COMISSAO_PLANEJ_URBANO_OBRAS_TRANSP(352,"Parecer da Comissão de Planejamento Urbano, Obras e Transporte"),
	PARECER_COMISSAO_SAUDE(354,"Parecer da Comissão de Saúde"),
	PARECER_JURIDICO(369,"Parecer Jurídico"),
	PROJETO_DECRETO_LEGISLATIVO(316,"Projeto de Decreto Legislativo "),
	PROJETO_LEI(348,"Projeto de Lei"),
	PROJETO_LEI_COMPLEMENTAR(367,"Projeto de Lei Complementar"),
	PROJETO_RESOLUCAO(344,"Projeto de Resolução"),
	SUBSTITUTIVO(345,"Substitutivo"),
	PROPOSTA_EMENTA_LEI_ORGANICA(346, "Proposta de Emenda a Lei Orgânica"),
	RECURSO_CONTRA_ATO_PRESIDENTE(406,"Recurso contra Ato do Presidente"),
	REDACAO_FINAL(359,"Redação Final"),
	SUBEMENDA(370,"Subemenda");
	
	private Integer code;
	private String text;
	
	Tipos(Integer code, String text) { 
		this.code = code;
		this.text = text;
	}
	
	/**
	 * @param text
	 * @return
	 */
	public static Optional<Tipos> findByText(final String text) { 
		return Stream.of(Tipos.values()).filter(t -> text.equals(t.text)).findFirst();
	}

	/**
	 * @return the code
	 */
	public Integer getCode() {
		return code;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

}
