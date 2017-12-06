package br.com.demoStore.core.selenium.command.exception;

/**
 * 
 * Name: {@link CommandException}
 * 
 * Propósito: Exception

 */
public class CommandException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Construtor
	 * @param msg
	 */
	public CommandException(String msg) {
		super(msg);
	}
	
	/**
	 * Construtor
	 * @param e
	 */
	public CommandException(Exception e) {
		super(e);
	}
	
	/**
	 * Construtor
	 * @param msg
	 * @param e
	 */
	public CommandException(String msg, Exception e) {
		super(msg, e);
	}

}
