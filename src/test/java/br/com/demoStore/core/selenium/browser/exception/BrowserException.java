package br.com.demoStore.core.selenium.browser.exception;

/**
 * 
 * Name: {@link BrowserException}
 * 
 * Propósito: Exception
 
 */
public class BrowserException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Construtor
	 * @param msg
	 */
	public BrowserException(String msg) {
		super(msg);
	}
	
	/**
	 * Construtor
	 * @param e
	 */
	public BrowserException(Exception e) {
		super(e);
	}
	
	/**
	 * Construtor
	 * @param msg
	 * @param e
	 */
	public BrowserException(String msg, Exception e) {
		super(msg, e);
	}

}
