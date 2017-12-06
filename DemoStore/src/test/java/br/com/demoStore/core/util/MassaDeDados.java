package br.com.demoStore.core.util;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import br.com.demoStore.core.tools.LoadWorkbook;

/**
 * Classe para recuperar os dados da planilha de "massa".
 *
 */
public class MassaDeDados {
	
	final static Logger logger = Logger.getLogger(MassaDeDados.class);
	private static Map<String, List<String>> dictionaryData;
	private static List<String[]> rows = new ArrayList<String[]>();;
	private static int totalRows;
	private static String[] columns;
	private static boolean noFilter;
	private static String currentCT;
	private static String pathFile;
	
	private MassaDeDados(){
 		totalRows =0;
 		noFilter = false;
 		currentCT = null;
	}

	public String getPath() throws Exception{
		if(pathFile != null){
			return pathFile;
		}
		throw new Exception("Caminho para a planilha de 'Data' não localizado, verifique se o caminho foi informado.");
	}
	
	/**
	 * Define que o sistema não deve realizar o filtro do nome do Caso de teste na Planilha.
	 *
	 */
	public static void setNoFilter() {
		noFilter = true;
	}
	
	/**
	 * Define que o sistema deve realizar o filtro do nome do Caso de teste na Planilha (Default do Sistema).
	 * 
	 */
	public static void setFilter() {
		noFilter = false;
	}

	/**
	 * Define qual o arquivo deve ser utilizado para carregar os dados.
	 * 
	 * @param Path
	 * @throws Exception
	 */
 	public static void setFile(String path) throws Exception{
 		pathFile = path;
		logger.debug("setFile : Path informado = " + path);
	}
	
	/**
	 * Define quais colunas devem ser carregadas da planilha de dados.
	 
	 * @param columnsNames
	 * @throws Exception
	 */
	public static void DefineColumns(String[] columnsNames ){
			columns = columnsNames;		    
	}
	
	/**
	 * @return String com o valor do primeiro indice de acordo com a key informada.
	 
	 * @param key
	 * @throws Exception
	 */
	public static String getValue(String key) throws Exception{
		filter();
		logger.debug("getValue(key = " + key + " index 0)");
		try{
			return dictionaryData.get(key).get(0).toString();
		}catch(NullPointerException e){
			String msg = "Não localizado a key " + key;
			logger.fatal(msg);
			throw new Exception(msg);
		}catch(IndexOutOfBoundsException e){
			String msg ="Indice 0 não existe para a key " + key;
			logger.fatal(msg);
			throw new Exception(msg);
		}
	}
	
	/**
	 * @return String com o valor de acordo com a Key e Index informado.
	 
	 * @param key
	 * @param index
	 * @throws Exception
	 */
	public static String getValue(String key, int index) throws Exception{
		filter();
		logger.debug("getValue(key = " + key + " index "+index+ ")");
		try{
			return dictionaryData.get(key).get(index).toString();
		}catch(NullPointerException e){
			String msg = "Não localizado a key " + key;
			logger.fatal(msg);
			throw new Exception(msg);
		}catch(IndexOutOfBoundsException e){
			String msg = "Indice "+ index +" não existe para a key " + key;
			logger.fatal(msg);
			throw new Exception(msg);
		}
		
	}
	
	/**
	 * @return List<Object> com os valores de acordo com a key informada.

	 * @param key
	 * @throws Exception
	 */
	public static List<String> getListOfValue(String key) throws Exception{
		filter();
		logger.debug("getValue(key = " + key + ")");		
		return dictionaryData.get(key);
	}
	
	/**
	 * @return int com a quantidade de linhas localizadas na planilha.

	 * @param key
	 * @throws Exception
	 */
	public static int length() throws Exception{		
		filter();
		logger.debug("Total de "+ (totalRows+1) +" elementos ");		
		return totalRows;
	}
	
	/**
	 * @return String[] que representa uma linha da planilha de acordo com o index informado.

	 * @param index
	 * @throws Exception 
	 */
	public static String[] getRow(int index) throws Exception{
		filter();
		loadRows();
		return rows.get(index);
	}
	
	/**
	 * @return List<String> lista com todas as linhas da planilha.

	 * @throws Exception 
	 */
	public static List<String[]> getRows() throws Exception{
		filter();
		loadRows();
		return rows;
	}
	
	/**
	 * carrega a lista de linhas da planilha.
	 */
	private static void loadRows(){
		rows.clear();
		if(rows.isEmpty()){
			for(int i = 0; i <= totalRows; i++){
				String[] row = new String[columns.length];
				int j =0;
				for(String column : columns){
					row[j] = dictionaryData.get(column).get(i);
					j++;
				}
				rows.add(row);
			}
		}
	}
	
	/**
	 * realiza o filtro de acordo com o nome do Caso de teste que está em execução.
	 * @throws Exception
	 */
	private static void filter() throws Exception{
		LoadWorkbook loadWorkbook = new LoadWorkbook();
		if(!noFilter){
			String test = Thread.currentThread().getName();
			if(currentCT != test){
				logger.debug("nome caso de teste diferente do atual '"+ currentCT +"' novo '"+ test + "'");
				dictionaryData = null;
				totalRows= 0;
				currentCT = test;
			}
			if(dictionaryData == null){
					loadWorkbook.setPath(pathFile);
					dictionaryData = loadWorkbook.loadData(columns, "Data", currentCT);
					totalRows = dictionaryData.get(columns[0]).size()-1;
					logger.debug("Realizado o load para o ct " + currentCT);
			}
		}
		else{
			if(dictionaryData == null){
				loadWorkbook.setPath(pathFile);
				dictionaryData = loadWorkbook.loadData(columns, "Data", null);
				totalRows = dictionaryData.get(columns[0]).size()-1;
				logger.debug("Realizado o load da planilha sem aplicar filtro.");
			}
		}
	}


}
