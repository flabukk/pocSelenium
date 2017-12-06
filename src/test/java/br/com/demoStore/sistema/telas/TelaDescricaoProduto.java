package br.com.demoStore.sistema.telas;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import br.com.demoStore.core.selenium.command.Command;
import br.com.demoStore.core.selenium.report.Report;

public class TelaDescricaoProduto extends Command {
		

	static final Logger logger = Logger.getLogger(TelaDescricaoProduto.class);

	private WebElement btnAdicionarCarrinho; 
	private WebElement nomeProduto; 
	private WebElement btnContinuarCompras; 
	
		
	public TelaDescricaoProduto(WebDriver webDriver, Report report) {
		super(webDriver, report);
		
	}
	
		
	/**Adiciona um produto ao carrinho 
	 * @throws Exception
	 */
	public void adicionarAoCarrinho() throws Exception{				
		clicarBotaoAdicionarAoCarrinho();
	}
	
		
	/**clicarBotaoAdicionarAoCarrinho
	 * 
	 */
	private void clicarBotaoAdicionarAoCarrinho(){
		btnAdicionarCarrinho = webDriver.findElement(By.xpath(".//button[@class='ty-btn__primary ty-btn__big ty-btn__add-to-cart cm-form-dialog-closer ty-btn']"));
		this.click(btnAdicionarCarrinho);
		logger.info("Clicado no botão [Adicionar ao Carrinho]");
		report.addStep("");
		report.addText("Clicado no botão [Adicionar ao Carrinho]");
		clicarBotaoContinuarCompras();
	}
	
	/**recuperarNomeProduto
	 * @return nome do produto
	 */
	public String recuperarNomeProduto(){
		nomeProduto = webDriver.findElement(By.xpath(".//*[@class='ty-product-block-title']"));
		logger.info("Nome do Produto recuperado: "+nomeProduto.getText());
		return nomeProduto.getText().toString();
	}
	
	/**clicarBotaoContinuarCompras
	 */
	private void clicarBotaoContinuarCompras() {
		
		Boolean exist = webDriver.findElements(By.xpath(".//*[@class='ty-btn ty-btn__secondary cm-notification-close ']")).size() > 0;
		if(exist){
			btnContinuarCompras = webDriver.findElement(By.xpath(".//*[@class='ty-btn ty-btn__secondary cm-notification-close ']"));
			this.click(btnContinuarCompras);
			logger.info("Clicado no botão [Continuar compras]");
		}		
	}
	
}
