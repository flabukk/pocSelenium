package br.com.demoStore.core.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

/**
 * Classe para manipular arquivos
 *
 *
 */
public class FileUtils {
	private static String pathFile;
	
	/**
	 * Delete files
	 * @param file
	 */
	public static void deleteFiles(String... file) {
		for (String f : file) {
			File fileStar = new File(f);
			File[] listFiles = fileStar.listFiles();
			if (listFiles != null) {
				for (File fl : listFiles) {
					fl.delete();
				}
			}
		}
	}
	

 	public String getPath() throws Exception{
		if(pathFile != null){
			return pathFile;
		}
		throw new Exception("Caminho para a planilha de 'Data' não localizado, verifique se o caminho foi informado.");
	}
 	
 	/**
	 * Create temp file
	 * @param clazz
	 * @param value
	 * @throws Exception
	 */
	public static void createFileTemp(String value) throws Exception {
		OutputStream out = new FileOutputStream("temp.txt");
		Writer writer = new OutputStreamWriter(out);
		BufferedWriter bufferedWriter = new BufferedWriter(writer);
		bufferedWriter.write(value);
		bufferedWriter.close();
	}
	
	/**
	 * Obter valor
	 * @return
	 * @throws Exception
	 */
	public static String getValue() throws Exception {
		InputStream in = new FileInputStream("temp.txt");
		Reader r = new InputStreamReader(in);
		BufferedReader b = new BufferedReader(r);
		return b.readLine();
	}
	
}
