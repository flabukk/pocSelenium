package br.com.demoStore.core.selenium.report;

import java.util.NoSuchElementException;

import org.openqa.selenium.WebDriver;

import br.com.demoStore.core.selenium.report.exception.ReportException;

/**
 * 
 * Name: {@link ReportFactory}

 *
 */
public class ReportFactory {

	/**
	 * Construtor privado. N�o � necess�rio instanciar classe
	 */
	private ReportFactory() {
	}

	/**
	 * Devolve a implementa��o da classe Report de acordo com o ReportType
	 * passado por par�metro.
	 * 
	 * @param type
	 * @param webDriver
	 * @return
	 */
	public static Report getReport(ReportType reportType, WebDriver webDriver) {
		return reportType.getReport(webDriver);
	}

	/**
	 * Devolve a implementa��o da classe Report de acordo com a op��o passada
	 * por par�metro como String.
	 * 
	 * @param type
	 *            O tipe de Report desejado
	 * @param webDriver
	 *            WebDriver utilizado no teste.
	 * @return Uma implementa��o da Report caso o type passado seja uma op��o
	 *         v�lida
	 * @throws NoSuchElementException
	 *             Quando n�o � poss�vel instanciar a op��o desejada.
	 */
	public static Report getReport(String reportType, WebDriver webDriver) {
		ReportType[] values = ReportType.values();
		for (int i = 0; i < values.length; i++) {
			if (values[i].toString().equalsIgnoreCase(reportType.trim())) {
				return getReport(values[i], webDriver);
			}
		}
		throw new ReportException("N�o foi poss�vel localizar nenhum Report do tipo " + reportType);
	}

}
