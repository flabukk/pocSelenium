package br.com.demoStore.core.selenium.command;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import br.com.demoStore.core.selenium.command.exception.CommandException;
import br.com.demoStore.core.selenium.report.Report;

/**
 * 
 * Name: {@link Command}
 * 
 * Propósito: Classe com o objetivo de encapsular os comandos básicos da
 * aplicação

 */
public abstract class Command {

	final static Logger logger = Logger.getLogger(Command.class);

	private final long TIME_OUT;
	
	protected String msgError;

	protected WebDriver webDriver;
	
	protected Report report;

	protected WebDriverWait webDriverWait;

	/**
	 * Construtor
	 * 
	 * @param webDriver
	 */
	public Command(WebDriver webDriver, Report report) {
		this.webDriver = webDriver;
		this.TIME_OUT = 30;
		this.report = report;	
		this.webDriverWait = new WebDriverWait(this.webDriver, TIME_OUT);
	}

	

	/**
	 * carrega o nome do texto no campo
	 * 
	 * 
	 * @param webElement
	 * @return
	 */
	public String getText(WebElement webElement) {
		
		return webElement.getText();
	}


	/**
	 * Enviar valor para os campos de 'input'
	 * 
	 * @param webElement
	 * @param value
	 */
	public void send(WebElement webElement, String value) {
		try {
			webElement = webDriverWait.until(ExpectedConditions.elementToBeClickable(webElement));
			webDriverWait.until(ExpectedConditions
					.invisibilityOfElementLocated(By.xpath("//div[contains(@id,'statusDialogWaiting')]")));

			if (webElement.getAttribute("class").contains("hasDatepicker"))
				value = value.replace("/", "");

			webElement.clear();
			webDriverWait.until(ExpectedConditions
					.invisibilityOfElementLocated(By.xpath("//div[contains(@id,'statusDialogWaiting')]")));
			webElement.sendKeys(value);
			logger.debug("Elemento: '" + webElement.toString() + "' informado: '" + value + "'");
			
		} catch (TimeoutException e) {
			msgError = "Erro TimeOut após aguardar " + TIME_OUT + " segundos, o elemento: '" + webElement.toString()
					+ "' não realizado ação 'send' no valor '" + value + "'";
			logger.fatal(msgError, e);
			throw new CommandException(msgError);
		} catch (StaleElementReferenceException e) {
			msgError = "Referencia do elemento: '" + webElement.toString()
					+ "' alterado durante a execução da ação 'send' no valor '" + value + "'";
			logger.fatal(msgError, e);
			throw new CommandException(msgError);
		}
	}

	

	/**
	 * Simular um click na tecla 'Enter'
	 * 
	 * @param webElement
	 */
	public void pressEnter(WebElement webElement) {
		try {
			webDriverWait.until(ExpectedConditions
					.invisibilityOfElementLocated(By.xpath("//div[contains(@id,'statusDialogWaiting')]")));
			webElement.sendKeys(Keys.ENTER);
			logger.debug("Elemento: '" + webElement.toString() + "' pressionado tecla 'ENTER'");
			
		} catch (Exception e) {
			msgError = "Elemento: '" + webElement.toString() + "' não pressionado tecla 'ENTER'";
			logger.fatal(msgError, e);
			throw new CommandException(msgError);
		}
	}

	
	/**
	 * Simular um click
	 * 
	 * @param webElement
	 */
	public void click(WebElement webElement) {
		try {
			webDriverWait.until(ExpectedConditions
					.invisibilityOfElementLocated(By.xpath("//div[contains(@id,'statusDialogWaiting')]")));
			webDriverWait.until(ExpectedConditions.visibilityOf(webElement));
			webDriverWait.until(ExpectedConditions.elementToBeClickable(webElement)).click();
			webDriver.switchTo().window(webDriver.getWindowHandle());
			logger.debug("Elemento: '" + webElement.toString() + "' realizado ação 'click'");
		} catch (Exception e) {
			msgError = "Elemento: '" + webElement.toString() + "' não realizado ação 'click'";
			logger.fatal(msgError, e);
			throw new CommandException(msgError);
		}
	}

	
	/**
	 * Aguardar
	 * 
	 * @param segundos
	 */
	public void waitCommand(int segundos) {
		try {
			Thread.sleep(1000 * segundos);
			logger.debug("Evento: Aguardar '" + segundos + "s' realizado");
		} catch (IllegalArgumentException e) {
			msgError = "Evento: Aguardar '" + segundos + "s' não realizado";
			logger.fatal(msgError, e);
			throw new CommandException(msgError);
		} catch (InterruptedException e) {
			msgError = "Evento: Aguardar '" + segundos + "s' não realizado";
			logger.fatal(msgError, e);
			throw new CommandException(msgError);
		}
	}

	
	
	/**
	 * Método para apagar os valores de um input
	 * 
	 * @param element
	 * 
	 */
	public void clear(WebElement element) {
		element.clear();
		webDriverWait.until(ExpectedConditions
				.invisibilityOfElementLocated(By.xpath("//div[contains(@id,'statusDialogWaiting')]")));
	}

	

}
