package br.com.demoStore.core.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateUtils {

	/**
	 * Retorna um texto com o nome do Mês em português de um objeto Date
	 * 
	 * @param date
	 *            Objeto date que deseja obter o mês
	 * @return String com o nome do Mês

	 */
	public static String getMount(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		DateFormat dateFormat = new SimpleDateFormat("MM");
		return mounthString(dateFormat.format(cal.getTime()));
	}

	/**
	 * Recebe uma String indicando o formato da data que se espera e um objeto
	 * do tipo Date. A função converte o objeto Date para uma String no formato
	 * recebido
	 * 
	 * @param format
	 *            String com o formato esperado para data
	 * @param date
	 *            objeto Date com a data que deseja formatar
	 * @return Retorna String com a data formatada
	 
	 */
	public static String formatDate(String format, Date date) {
		DateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(date);
	}

	/**
	 * Retorna um texto com o nome do Mês abreviado e em português de um objeto
	 * Date
	 * 
	 * @param date
	 *            Objeto date que deseja obter o mês abreviado
	 * @return String com o nome do Mês abreviado
	
	 */
	public static String getAbbreviatedMounth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		DateFormat dateFormat = new SimpleDateFormat("MM");
		String mounth = mounthString(dateFormat.format(cal.getTime()));
		return mounth.substring(0, 3);
	}

	/**
	 * Retorna o número do Mês de um objeto Date
	 * 
	 * @param date
	 *            Objeto date que deseja obter o número do mês
	 * @return String com o número do Mês

	 */
	public static String getMounthNumber(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		DateFormat dateFormat = new SimpleDateFormat("MM");
		return dateFormat.format(cal.getTime());

	}

	/**
	 * Método auxiliar para devolver o nome do Mês em portugues
	 * 
	 * @param mounthNumber
	 *            Número do mês que deseja obter o nome em português
	 * @return Nome do mês em portugues
	 * @throws IllegalArgumentException
	 *             caso o número do mês informado não seja válido
	 */
	public static String mounthString(String mounthNumber) {
		switch (mounthNumber) {
		case "01":
			return "Janeiro";
		case "02":
			return "Fevereiro";
		case "03":
			return "Março";
		case "04":
			return "Abril";
		case "05":
			return "Maio";
		case "06":
			return "Junho";
		case "07":
			return "Julho";
		case "08":
			return "Agosto";
		case "09":
			return "Setembro";
		case "10":
			return "Outubro";
		case "11":
			return "Novembro";
		case "12":
			return "Dezembro";
		default:
			throw new IllegalArgumentException("Mês " + mounthNumber + " não é um mês válido");
		}
	}
	
	/**
	 * Método auxiliar para devolver o numero do Mês 
	 * 
	 * @param mounth
	 *            Nome do mês em português que deseja obter o numero
	 * @return Numero do mês
	 * @throws IllegalArgumentException
	 *             Caso o nome do mês informado não seja válido
	 */
	public static Integer getNumberMonthString(String mounth) {
		switch (mounth) {
		case "Janeiro":
			return 1;
		case "Fevereiro":
			return 2;
		case "Março":
			return 3;
		case "Abril":
			return 4;
		case "Maio":
			return 5;
		case "Junho":
			return 6;
		case "Julho":
			return 7;
		case "Agosto":
			return 8;
		case "Setembro":
			return 9;
		case "Outubro":
			return 10;
		case "Novembro":
			return 11;
		case "Dezembro":
			return 12;
		default:
			throw new IllegalArgumentException("Mês " + mounth + " não é um mês válido");
		}
	}

	public static Date getDate(String formatDate, String date) throws ParseException {
		SimpleDateFormat dt = new SimpleDateFormat(formatDate);
		return dt.parse(date);
	}

	public static Date sumDaysToDate(Date date, int days) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, days);
		return c.getTime();
	}

	public static String changeFormat(String actualFormat, String expectedResult, String date) throws ParseException {
		Date d = getDate(actualFormat, date);
		return formatDate(expectedResult, d);
	}

	/**
	 * Transforma uma data que vem em date para Calendar
	 * 
	 * @param date
	 * @return um objeto Calendar
	 */
	public static Calendar getCalendar(Date date) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		return calendar;

	}

	/**
	 * Transforma uma data que vem em string para DateTime
	 * 
	 * @param formatDate
	 * @param date
	 * @return um objeto DateTime
	 */
	public static DateTime getDateTime(String formatDate, String date) {
		DateTimeFormatter formatter = DateTimeFormat.forPattern(formatDate);
		return formatter.parseDateTime(date);
	}

	/**
	 * @param finalDate
	 * @param initialDate
	 * @return
	 */
	public static int subtractDates(String finalDate, String initialDate) {
		DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy");
		DateTime dt1 = DateTime.parse(finalDate, formatter);
		DateTime dt2 = DateTime.parse(initialDate, formatter);
		return Days.daysBetween(dt1, dt2).getDays();
	}

	/**
	 * Função que realiza a contagem de dias entre duas datas.
	 * 
	 * @param initialDate
	 * @param finalDate
	 * @return
	 */
	public static int contaAQuantidadeDiasEntreD1eD2(String initialDate, String finalDate) {
		DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy");
		DateTime dt1 = DateTime.parse(initialDate, formatter);
		DateTime dt2 = DateTime.parse(finalDate, formatter);
		return Days.daysBetween(dt1, dt2).getDays();
	}

	/**
	 * Função que insere '/' em uma data vinda do arquivo no formato ddmmyyyy
	 * 
	 * @param data
	 *            - no formato ddmmyyyy
	 * @return
	 */
	public static String formatDateFromFile(String data) {
		String dateFormated = "";
		dateFormated = data.substring(0, 2) + "/" + data.substring(2, 4) + "/" + data.substring(4, 8);
		return dateFormated;
	}
	
	/**
	 * Função que recebe data no formato yyyyMMdd e transforma em dd/MM/yyyy
	 * 
	 * @param data
	 *            - no formato yyyyMMdd
	 * @return
	 */
	public static String formaraDataDoArquivo(String data) {
		String dateFormated = "";
		dateFormated = data.substring(6, 8) + "/" + data.substring(4, 6) + "/" + data.substring(0, 4);
		return dateFormated;
	}

	/**
	 * Transforma uma data para calendar para usar em comparações.
	 */
	public static Calendar getDataFromCalendar(Date date) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(GregorianCalendar.HOUR_OF_DAY, 0);
		calendar.set(GregorianCalendar.MINUTE, 0);
		calendar.set(GregorianCalendar.SECOND, 0);
		calendar.set(GregorianCalendar.MILLISECOND, 0);
		
		return calendar;
	}

}
