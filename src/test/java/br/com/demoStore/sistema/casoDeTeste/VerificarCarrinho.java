package br.com.demoStore.sistema.casoDeTeste;

import java.nio.file.Paths;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import br.com.demoStore.core.junit.TestRunner;
import br.com.demoStore.core.junit.Watcher;
import br.com.demoStore.core.selenium.browser.WebDriverFactory;
import br.com.demoStore.core.selenium.report.Report;
import br.com.demoStore.core.selenium.report.ReportFactory;
import br.com.demoStore.core.selenium.report.ReportType;
import br.com.demoStore.core.util.MassaDeDados;
import br.com.demoStore.sistema.telas.TelaConteudoCarrinho;
import br.com.demoStore.sistema.telas.TelaDescricaoProduto;
import br.com.demoStore.sistema.telas.TelaHome;


@RunWith(TestRunner.class)
public class VerificarCarrinho {
	
	@Rule public Watcher watcher = new Watcher();

	static final Logger logger = Logger.getLogger(VerificarCarrinho.class);
		
	private static WebDriver webDriver;	
	private static Report report;
		
	// ------------------------------------------------------------------------------------------
	// Define Classes de navegação e suas dependencias
	// ------------------------------------------------------------------------------------------
	
	TelaHome home = new TelaHome(webDriver, report);
	TelaDescricaoProduto telaDescricaoProduto = new TelaDescricaoProduto(webDriver, report);
	TelaConteudoCarrinho telaConteudoCarrinho = new TelaConteudoCarrinho(webDriver, report);	
	
	@BeforeClass
	public static void setupTest() throws Throwable {
		PropertyConfigurator.configure(Paths.get("").toAbsolutePath().toString() + "/log4j.properties");
		
		MassaDeDados.setFile(Paths.get("").toAbsolutePath().toString() + "\\resource\\files\\VerificarCarrinho.xlsx");
		MassaDeDados.DefineColumns(new String[] { "url","produto1","produto2"});
		webDriver = WebDriverFactory.getWebDriver("internetexplorer", "localhost");
		report = ReportFactory.getReport(ReportType.PDF, webDriver);
	}
	
	@AfterClass
	public static void tearDownTest() {
		
		if (webDriver != null){
			webDriver.close();
			webDriver.quit();
		}
	}
	
	@Test
	public void CT_001_VerificarCarrinho() throws Exception{
				
			//Dados para evidencias
			report.setCover("CT_001_VerificarCarrinho", "Objetivo: Adicionar e verificar produtos adicionados no carrinho");
				
			report.addText("1. Acessar o site: http://demo.cs-cart.com/");
			home.openBrowser(MassaDeDados.getValue("url"));
			

			report.addText("2. No campo 'Procurar Produtos' pesquise usando o valor 'Batman'.");			
			home.realizarPesquisa(MassaDeDados.getValue("produto1"));
			
			
			report.addText("3. No resultado da pesquisa clique em um dos resultados apresentados.");
			home.selecionarProduto(MassaDeDados.getValue("produto1"));
			
			String nomeProduto1 = telaDescricaoProduto.recuperarNomeProduto();
			
			report.addText("4. Na tela do produto pressione o botão 'Adicionar ao carrinho'.");
			telaDescricaoProduto.adicionarAoCarrinho();
			
			report.addText("5. Repita os passos 2 a 4, pesquisando pelo item 'PS3'.");
			home.realizarPesquisa(MassaDeDados.getValue("produto2"));
			home.selecionarProduto(MassaDeDados.getValue("produto2"));
			
			String nomeProduto2 = telaDescricaoProduto.recuperarNomeProduto();
			telaDescricaoProduto.adicionarAoCarrinho();
			
			
			report.addText("6. Valide no carrinho de compras se os produtos foram adicionados com sucesso.");
			home.clicarBotaoMeuCarrinho();
			home.clicarBotaoVerCarrinho();
			
			telaConteudoCarrinho.validarProdutoNoCarrinho(nomeProduto1);
			telaConteudoCarrinho.validarProdutoNoCarrinho(nomeProduto2);
				
								
	}
	
}