package br.com.demoStore.sistema.telas;

import static org.junit.Assert.fail;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import br.com.demoStore.core.selenium.command.Command;
import br.com.demoStore.core.selenium.report.Report;

public class TelaHome extends Command{
	
	static final Logger logger = Logger.getLogger(TelaHome.class);
		
	private WebElement campoPesquisa; 
	private WebElement btnLupaPesquisa; 
	private WebElement btnMeuCarrinho; 
	private WebElement btnVerCarrinho; 
	
	
	public TelaHome (WebDriver webDriver, Report report) {
		super (webDriver, report);
		new  WebDriverWait (webDriver, 30);		
	}
	
	
	/**openBrowser
	 * @param url
	 */
	public void openBrowser (String url){
		
		webDriver.navigate().to(url);
		logger.info("Abre o site: " + url);
		
		if (!webDriver.getCurrentUrl().contains(url)) {
			String messageLog = "Não foi possível acessar o sistema! URL Atual:"+ webDriver.getCurrentUrl() ;
			logger.error(messageLog);
			fail(messageLog);
		}	
		report.addStep("");
		report.addText("Browser aberto com a url: "+url);
	}
	
	
	/**realizarPesquisa
	 * @param produto
	 * @throws Exception
	 */
	public void realizarPesquisa(String produto) throws Exception{		
			
		this.digitarPesquisa(produto);		
		this.clicarLupa();
		report.addStep("");
		report.addText("Realizado pesquisa do Produto: "+produto);
	}
	
	
	/**preenche campo pesquisa
	 * @param produto
	 */
	public void digitarPesquisa(String produto){
			
		campoPesquisa = webDriver.findElement(By.xpath(".//*[@id='search_input']"));
		this.send(campoPesquisa, produto);
		logger.info("Campo pesquisa preenchido com o valor: " + produto);
	
	}
	
	
	/**Clica na lupa de pesquisa
	 * 
	 */
	private void clicarLupa(){
		btnLupaPesquisa = webDriver.findElement(By.xpath(".//*[@title='Pesquisar']"));
		this.click(btnLupaPesquisa);
		logger.info("Clicado na Lupa de Pesquisa");
		
	}

	/**Seleciona um produto à partir de uma parte do nome
	 * @param parteNomeProduto
	 * @throws InterruptedException 
	 */
	public void selecionarProduto(String parteNomeProduto) throws InterruptedException{		
		 WebElement produto = webDriver.findElement(By.partialLinkText(parteNomeProduto));
		this.click(produto);
		logger.info("Selecionado o produto: " + parteNomeProduto);
		report.addStep("");
		report.addText("Selecionado o produto: " + parteNomeProduto);
	}
	
	
	/**clicarBotaoMeuCarrinho
	 * @throws InterruptedException 
	 * 
	 */
	public void clicarBotaoMeuCarrinho() throws InterruptedException{
		btnMeuCarrinho = webDriver.findElement(By.xpath(".//*[@class='ty-minicart__icon ty-icon-moon-commerce filled']"));
		this.click(btnMeuCarrinho);
		logger.info("Clicado no botão [Meu Carrinho]");
		report.addStep("");
		report.addText("Clicado no botão [Meu Carrinho]");
	}
	
	
	/**clicarBotaoVerCarrinho
	 * @throws InterruptedException 
	 * 
	 */
	public void clicarBotaoVerCarrinho() throws InterruptedException{
		btnVerCarrinho = webDriver.findElement(By.xpath(".//*[@class='ty-btn ty-btn__secondary']"));
		this.click(btnVerCarrinho);
		logger.info("Clicado no botão [Ver Carrinho]");
		report.addStep("");
		report.addText("Clicado no botão [Ver Carrinho]");
	}
	
}
