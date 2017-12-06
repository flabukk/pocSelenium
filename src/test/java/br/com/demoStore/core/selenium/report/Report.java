package br.com.demoStore.core.selenium.report;

import org.openqa.selenium.WebElement;

import com.itextpdf.text.BaseColor;

/**
 * 
 * Name: {@link Report}
 * 
 * Prop�sito: Interface com o objetivo de encapsular as funcionalidades de cria��o das evidencias
 * 
 */
public interface Report {

	/**
	 * Captura a tela atual e empilha a imagem ao Report sem precisar incluir o texto.
	 */
	public void addStep();
	
	/**
	 * Captura a tela atual e empilha a imagem ao Report junto � descri��o
	 * recebida por par�metro.
	 * 
	 * @param description
	 *            Texto que ser� adicionado ao report junto � imagem
	 */
	public void addStep(String description);

	/**
	 * Captura a tela atual e empilha a imagem ao Report junto � descri��o
	 * recebida por par�metro. Permite tamb�m desativar temporariamente a utiliza��o do texto padr�o.
	 * 
	 * @param description
	 * 			Texto que ser� adicionado ao report junto � imagem	
	 * @param discartStandardText
	 * 			Se o valor for True, n�o adiciona o texto padr�o que foi incluido no metodo EvidenceManager.addTextReport.
	 */
	public void addStep(String description, boolean discartStandardText);

	/**
	 * Recebe os elementos da tela que devem ser destacados, captura a tela e
	 * empilha a imagem ao Report. Ap�s
	 * a captura da tela os highlights devem ser removidos.
	 * 
	 * @param WebElement[]
	 *            Lista de WebElements que devem ser destacados.
	 */
	public void addStep(WebElement... webElement);
	
	/**
	 * Recebe os elementos da tela que devem ser destacados, captura a tela e
	 * empilha a imagem ao Report junto � descri��o recebida por par�metro. Ap�s
	 * a captura da tela os highlights devem ser removidos.
	 * 
	 * @param description
	 *            Texto que ser� adicionado ao report junto � imagem
	 * @param WebElement[]
	 *            Lista de WebElements que devem ser destacados.
	 */
	public void addStep(String description, WebElement... webElement);

	
	/**
	 * Define qual o local o Report deve ser salvo.
	 * 
	 * Caso n�o seja definido, usar� como default o diret�rio de execu��o.
	 * 
	 * @param path
	 *            Path indicando qual diret�rio o Report deve ser salvo.
	 */
	public void setPath(String path);

	/**
	 * Define o nome do teste e o objetivo dele na capa do Report. Caso n�o seja
	 * definido nenhum valor e capa ser� adicionada em branco.
	 * 
	 * @param testName
	 * @param objective
	 */
	public void setCover(String testName, String objective);

	/**
	 * Salva a evidencia em caminho previamente especificado. Caso n�o tenha
	 * sido especificado um caminho, ent�o salvar� no diret�rio de execu��o
	 */
	public void save();

	/**
	 * Salva a evidencia em caminho previamente especificado. Caso n�o tenha
	 * sido especificado um caminho, ent�o salvar� no diret�rio de execu��o
	 */
	public void save(String path);

	/**
	 * Adicionar um texto � Pagina
	 * 
	 * @param description
	 *            Texto que ser� adicionado ao report junto � imagem
	 */
	public void addText(String description);

	public void disableStandardText();
	
	/**
	 * 
	 */
	public void save (boolean isPassed);
	
	/**
	 * Adicionar um texto � mesma p�gina
	 * 
	 * @param description
	 *            Texto que ser� adicionado ao report junto � imagem
	 * @param fontName
	 *            Define o nome da fonte
	 * @param size
	 *            Define tamanho da fonte
	 * @param style
	 *            Define o estilo da fonte ex:(Italic, bold)
	 * @BaseColor 
	 *            Define a cor da fonte
	 * Exemplo de uso: report.addFreeText("EXAMPLE", FontFactory.HELVETICA, 12, Font.BOLD, BaseColor.GREEN);
	 * 
	 */
	public void addFreeText(String description, String fontName, float size, int style, BaseColor color);
	
	
}
