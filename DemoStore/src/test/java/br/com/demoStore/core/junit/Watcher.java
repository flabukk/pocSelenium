package br.com.demoStore.core.junit;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Paths;
import java.util.Date;

import org.apache.log4j.Logger;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import br.com.demoStore.core.selenium.report.EvidenceManager;
import br.com.demoStore.core.util.DateUtils;


public class Watcher extends TestWatcher {

	static final Logger logger = Logger.getLogger(Watcher.class);
	


	public Watcher() {
	}

	protected void starting(Description description) {
		Thread.currentThread().setName(description.getMethodName());
	}

	

	/**
	 * @param t
	 * @param nameCaseTest
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void exportStackTrace(Throwable t, String nameCaseTest) throws FileNotFoundException, IOException {
		String path = Paths.get("").toAbsolutePath().toString() + "//Evidences//" + nameCaseTest
				+ DateUtils.formatDate("yyyyMMddhhmmss", new Date()) + ".txt";
		FileOutputStream file = new FileOutputStream(path);
		PrintStream s = new PrintStream(file, true);
		t.printStackTrace(s);
		file.close();
		EvidenceManager.addEvidence(Paths.get(path));
	}

	

	@Override
	protected void finished(Description description) {
		logger.info("Finished - " + description.getMethodName());
	}
}
