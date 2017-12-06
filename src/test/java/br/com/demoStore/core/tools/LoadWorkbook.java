package br.com.demoStore.core.tools;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Classe que realiza o load dos arquivos do tipo workbook.
 *
 */
public class LoadWorkbook {
	final static Logger logger = Logger.getLogger(LoadWorkbook.class);
	private Workbook workbook;
	private String messageError;

	public LoadWorkbook() {

	}

	public LoadWorkbook(String path) throws Exception {
		this.setPath(path);
	}

	/**
	 * Metodo que define qual o arquivo deve ser utilizado.
	 * @param path
	 * @throws Exception
	 */
	public void setPath(String path) throws Exception {
		try {
			workbook = WorkbookFactory.create(new FileInputStream(path.replace("\\", "//")));
		} catch (EncryptedDocumentException | InvalidFormatException | IOException e) {
			throw new Exception("Falha ao tentar abrir o arquivo " + path
					+ "\n Verifique se o mesmo existe ou está aberto por outro recurso.");
		}
		logger.debug("setFile : Path informado = " + path);
	}

	/**
	 * Metodo para realizar a leitura de informações em uma planilha que
	 * contenha uma aba com colunas de nome 'key' e 'value'.
	 * @param nameSheet
	 * @return HashMap<String, Object> com os valores Key e Value.
	 * @throws Exception
	 */
	public synchronized HashMap<String, String> loadDictionary(String nameSheet) throws Exception {
		HashMap<String, String> dictionary = new HashMap<String, String>();
		Sheet sheet = workbook.getSheet(nameSheet);
		if (sheet == null) {
			throw new Exception("Falha ao tentar abrir a sheet " + nameSheet);
		}
		logger.debug("Localizada a aba " + sheet.getSheetName());
		int colKeyNum = -1;
		int colValueNum = -1;
		for (int rowID = 0; rowID <= sheet.getLastRowNum(); rowID++) {
			Row row = sheet.getRow(rowID);
			if (row != null) {
				if (row.getRowNum() == 0) {
					colKeyNum = getColNum(row, "key");
					colValueNum = getColNum(row, "value");
					logger.debug("Localizado key e value na aba " + sheet.getSheetName());
				}
				if (row.getCell(colKeyNum) != null) {
					if (row.getCell(colKeyNum).getCellType() == Cell.CELL_TYPE_STRING) {
						logger.debug(String.format("key localizada = %s", row.getCell(colKeyNum).getStringCellValue()));
						dictionary.put(row.getCell(colKeyNum).getStringCellValue(),
								getValueCell(row.getCell(colValueNum)));
					} else {
						messageError = String.format(
								"Linha %s, Coluna %s com tipo de celula invalida na aba %s, somente é permitido texto para a coluna key.",
								(row.getRowNum() + 1), colKeyNum, sheet.getSheetName());
						logger.fatal(messageError);
						throw new Exception(messageError);
					}
				} else {
					logger.debug(String.format(
							"Aba %s, Linha %s, Coluna %s celula com valor nulo.{Esperado:Cell_Type_String}",
							sheet.getSheetName(), (row.getRowNum() + 1), colKeyNum));
				}
			}
		}
		logger.debug("Finalizado Leitura da aba " + sheet.getSheetName());
		workbook.close();
		return dictionary;
	}

	/**
	 * Metodo para realizar a leitura de informações em uma planilha que
	 * contanha em sua aba colunas com nomes contindos no parametro
	 * 'namesColumns'.
	 * @param namesColumns
	 * @param nameSheet
	 * @return HashMap<String, List<Object>> com uma lista de valores.
	 * @throws Exception
	 */
	public synchronized HashMap<String, List<String>> loadData(String[] namesColumns, String nameSheet, String filter)
			throws Exception {
		HashMap<String, List<String>> Data = new HashMap<String, List<String>>();
		Sheet sheet = workbook.getSheet(nameSheet);
		if (sheet == null) {
			throw new Exception("Falha ao tentar abrir a sheet " + nameSheet);
		}
		int columnNumFilter = getColNum(sheet.getRow(0), "ct");
		if (namesColumns != null) {
			for (String nameColumn : namesColumns) {
				List<String> listItem = new ArrayList<String>();
				int columnNum = getColNum(sheet.getRow(0), nameColumn);
				for (int i = 1; i <= sheet.getLastRowNum(); i++) {
					if (sheet.getRow(i) != null) {
						if (filter == null) {
							listItem.add(getValueCell(sheet.getRow(i).getCell(columnNum)));
						} else if (getValueCell(sheet.getRow(i).getCell(columnNumFilter)).toLowerCase()
								.contains(filter.toLowerCase())) {
							listItem.add(getValueCell(sheet.getRow(i).getCell(columnNum)));
						}
					} else {
						logger.debug("linha " + (i + 1) + " sem valores");
					}
				}
				Data.put(nameColumn, listItem);
			}
		} else {
			throw new Exception("Não foi definido nenhuma coluna para busca de valores!");
		}
		workbook.close();
		return Data;
	}

	/**
	 * Retorna uma abastrção de uma planilha onde a key é o nome da sheet e a
	 * lista contem os valores das celulas.
	 * 
	 * @return HashMap<String, List<List<String>>>
	 * @throws Exception
	 */
	public synchronized HashMap<String, List<List<String>>> loadData() throws Exception {
		HashMap<String, List<List<String>>> Data = new HashMap<String, List<List<String>>>();
		for (Sheet sheet : workbook) {
			List<List<String>> listItem = new ArrayList<List<String>>();
			for (int i = 0; i <= sheet.getLastRowNum(); i++) {
				if (sheet.getRow(i) != null) {
					List<String> cels = new ArrayList<String>();
					for (int j = 0; j <= sheet.getRow(i).getLastCellNum(); j++) {
						cels.add(getValueCell(sheet.getRow(i).getCell(j)));
					}
					listItem.add(cels);
				}
			}
			Data.put(sheet.getSheetName(), listItem);
			workbook.close();
		}
		return Data;
	}

	/**
	 * @param row
	 * @param nameCol
	 * @return int com o numero da coluna que contem o texto informado.
	 * @throws Exception
	 */
	private int getColNum(Row row, String nameCol) throws Exception {
		if (row != null) {
			for (int colID = 0; colID <= (row.getLastCellNum() - 1); colID++) {
				if (row.getCell(colID) != null) {
					if (row.getCell(colID).getCellType() == Cell.CELL_TYPE_STRING) {
						if (row.getCell(colID).getStringCellValue().toLowerCase().equals(nameCol.toLowerCase())) {
							logger.debug("Localizado o título " + nameCol + " na coluna de número " + colID);
							return colID;
						}
					}
				}
			}
		}
		messageError = "Não foi localizado a coluna com o título igual a " + nameCol;
		logger.fatal(messageError);
		throw new Exception(messageError);
	}

	/**
	 * @param cell
	 * @return String com o valor da celula localizada.
	 * @throws Exception
	 */
	private String getValueCell(Cell cell) throws Exception {
		if (cell != null) {
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_STRING:
				logger.debug(String.format("Value STRING localizada = %s", cell.getStringCellValue()));
				return cell.getStringCellValue();

			case Cell.CELL_TYPE_NUMERIC:
				if (HSSFDateUtil.isCellDateFormatted(cell)) {
					logger.debug(String.format("Value DATE localizada = %s", String.valueOf(cell.getDateCellValue())));
					return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(cell.getDateCellValue());
				}
				logger.debug(
						String.format("Value NUMERIC localizada = %s", String.valueOf(cell.getNumericCellValue())));
				Double d = new Double(String.valueOf(cell.getNumericCellValue()));
				if (d % 1 == 0)
					return String.valueOf(d.longValue());
				else
					return String.valueOf(d);

			case Cell.CELL_TYPE_BOOLEAN:
				logger.debug(
						String.format("Value BOOLEAN localizada = %s", String.valueOf(cell.getBooleanCellValue())));
				return String.valueOf(cell.getBooleanCellValue());

			case Cell.CELL_TYPE_FORMULA:
				logger.debug(String.format("Value FORMULA localizada = %s", String.valueOf(cell.getCellFormula())));
				return getValueCell(workbook.getCreationHelper().createFormulaEvaluator().evaluateInCell(cell));

			case Cell.CELL_TYPE_BLANK:
				logger.debug(String.format("Value BLANK localizada = %s", String.valueOf(cell.getStringCellValue())));
				return cell.getStringCellValue();
			}
		} else {
			logger.debug("Value NULL localizada");
			return new String();
		}
		messageError = String.format("Linha %s, Coluna %s com tipo de celula invalida.", (cell.getRowIndex() + 1),
				cell.getColumnIndex());
		logger.fatal(messageError);
		throw new Exception(messageError);
	}

	public static List<List<String>> loadCSV(String path) throws Exception {
		return loadCSV(path, null, 0);
	}

	public static List<List<String>> loadCSV(String path, String filter) throws Exception {
		return loadCSV(path, filter, 0);

	}

	public static List<List<String>> loadCSV(String path, String filter, int columnFilter) throws Exception {
		List<List<String>> CSV = new ArrayList<List<String>>();
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
			String row = "";
			while ((row = bufferedReader.readLine()) != null) {
				String[] rowSplit = row.split(";");
				if (rowSplit.length > 0) {
					List<String> Columns = new ArrayList<String>();
					if (rowSplit[columnFilter].toLowerCase().contains(filter.toLowerCase())) {
						for (String column : rowSplit) {
							if (!column.contains(filter)) {
								Columns.add(column);
							}
						}
						CSV.add(Columns);
					}
				}
			}
			bufferedReader.close();
		} catch (Exception e) {
			throw new Exception("Não foi possivel realizar a leitura do arquivo " + path);
		}
		return CSV;
	}

	/**
	 * Metodo para realizar a leitura de informações em uma planilha que
	 * contanha em sua aba colunas com nomes contindos no parametro
	 * 'namesColumns'.
	 * @param namesColumns
	 * @param nameSheet
	 * @return JsonArray com uma lista de valores.
	 * @throws Exception
	 */
	public synchronized JsonArray loadJsonData(String nameSheet, String... columns) throws Exception {
		JsonArray jsonArray = new JsonArray();
		Sheet sheet = workbook.getSheet(nameSheet);
		if (sheet == null) {
			workbook.close();
			throw new Exception("Falha ao tentar abrir a sheet " + nameSheet);
		}
		if (columns != null) {
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				JsonObject json = new JsonObject();
				json.addProperty("RowNumber", i);
				for (String column : columns) {
					if (sheet.getRow(i) != null) {
						json.addProperty(column, getValueCell(sheet.getRow(i).getCell(getColNum(sheet.getRow(0), column))));
					} else {
						logger.debug("linha " + (i + 1) + " sem valores");
						json.addProperty(column, "");
					}
				}
				jsonArray.add(json);
			}
		} else {
			workbook.close();
			throw new Exception("Não foi definido nenhuma coluna para busca de valores!");
		}
		workbook.close();
		return jsonArray;
	}

}
