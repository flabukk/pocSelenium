package br.com.demoStore.core.selenium.report;

import org.openqa.selenium.WebDriver;

/**
 * 
 * Name: {@link ReportType}
 * 
 * Propósito: Enum de configuração com os tipos de extensao de reports
 */
public enum ReportType {
	
	PDF {
		@Override
		public Report getReport(WebDriver webDriver) {
			return new ReportPDF(webDriver);
		}
	};
	
	
	/**
	 * Obter report
	 * @param driver
	 * @return
	 */
	public abstract Report getReport (WebDriver driver);
}
