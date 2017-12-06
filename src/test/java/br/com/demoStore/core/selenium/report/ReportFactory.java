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
	 * Construtor privado. Não é necessário instanciar classe
	 */
	private ReportFactory() {
	}

	/**
	 * Devolve a implementação da classe Report de acordo com o ReportType
	 * passado por parâmetro.
	 * 
	 * @param type
	 * @param webDriver
	 * @return
	 */
	public static Report getReport(ReportType reportType, WebDriver webDriver) {
		return reportType.getReport(webDriver);
	}

	/**
	 * Devolve a implementação da classe Report de acordo com a opção passada
	 * por parâmetro como String.
	 * 
	 * @param type
	 *            O tipe de Report desejado
	 * @param webDriver
	 *            WebDriver utilizado no teste.
	 * @return Uma implementação da Report caso o type passado seja uma opção
	 *         válida
	 * @throws NoSuchElementException
	 *             Quando não é possível instanciar a opção desejada.
	 */
	public static Report getReport(String reportType, WebDriver webDriver) {
		ReportType[] values = ReportType.values();
		for (int i = 0; i < values.length; i++) {
			if (values[i].toString().equalsIgnoreCase(reportType.trim())) {
				return getReport(values[i], webDriver);
			}
		}
		throw new ReportException("Não foi possível localizar nenhum Report do tipo " + reportType);
	}

}
