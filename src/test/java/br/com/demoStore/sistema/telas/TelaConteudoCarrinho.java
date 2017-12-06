package br.com.demoStore.sistema.telas;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import br.com.demoStore.core.selenium.command.Command;
import br.com.demoStore.core.selenium.report.Report;

public class TelaConteudoCarrinho extends Command{
	
	static final Logger logger = Logger.getLogger(TelaConteudoCarrinho.class);
	
	
	public TelaConteudoCarrinho (WebDriver webDriver, Report report) {
		super (webDriver, report);
		new  WebDriverWait (webDriver, 30);
		
	}
	
	
	/**valida se existe o produto no carrinho
	 * @param nomeProduto
	 */
	public void validarProdutoNoCarrinho(String nomeProduto) {
		try {
			 webDriver.findElement(By.xpath("//*[contains(text(),'" + nomeProduto + "')]"));
			Assert.assertTrue("Produto: ["+nomeProduto+"] adicionado no carrinho.", true);
			logger.info("Produto: ["+nomeProduto+"] adicionado no carrinho.");	
			//report.addStep("Produto: ["+nomeProduto+"] adicionado no carrinho.");
		} catch (NoSuchElementException e) {
			Assert.assertTrue("Produto: ["+nomeProduto+"] NÃO EXISTE no carrinho.", false);
			logger.info("Produto: ["+nomeProduto+"] NÃO EXISTE no carrinho.");	
			//report.addStep("Produto: ["+nomeProduto+"] NÃO EXISTE no carrinho.");
		}
	}
	
}
