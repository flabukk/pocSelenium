package br.com.demoStore.core.selenium.report.exception;

/**
 * 
 * Name: {@link ReportException}
 * 
 * Propósito: Exception

 *
 */
public class ReportException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Construtor
	 * @param msg
	 */
	public ReportException(String msg) {
		super(msg);
	}
	
	/**
	 * Construtor
	 * @param e
	 */
	public ReportException(Exception e) {
		super(e);
	}
	
	/**
	 * Construtor
	 * @param msg
	 * @param e
	 */
	public ReportException(String msg, Exception e) {
		super(msg, e);
	}

}
