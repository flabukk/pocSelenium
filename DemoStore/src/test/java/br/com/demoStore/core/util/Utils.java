package br.com.demoStore.core.util;

import com.google.gson.JsonElement;

/**
 * Name: {@link Utils}
 * 
 * Propósito: Classe, com funções utilitárias
 */
public final class Utils {
	
	/**
	 * Formatar string (mask currency) para double
	 * @param value
	 * @return
	 */
	public static double formatPreco(String value) {
		if (value.isEmpty())
			return 0D;
		return Double.valueOf(formatValue(value));
	}
	
	private static String formatValue(String value) {
		String result = value.replaceAll("\\s*R\\$\\s*", "").replaceAll("\\.", "").replaceAll(",", "\\.");
		return result;
	}
	
	/**
	 * Verifica se o campo é null ou vazio
	 * @param param
	 * @return
	 */
	public static boolean isEmptyOrNull(String param) {
		return param != null && param.isEmpty();
	}
		
	/**
	 * Verifica se o campo não é null ou vazio
	 * @param param
	 * @return
	 */
	public static boolean isNotEmptyOrNull(String param) {
		return param != null && !param.isEmpty();
	}
	
	/**
	 * Este método valida apenas a empresa adquirente
	 * @param param
	 * @return
	 */
	public static boolean validarApenasEmpresaAdq(String param) {
		if (param.equals("") || param.equals("001") || param.equals("002")) {
			return true;
		}
		return false;
	}
	
	/**
	 * Obter o valor do campo sem espaço
	 * @param element
	 * @return
	 */
	public static String getValue(JsonElement element) {
		return element.getAsString().trim();
	}
	
}
