package br.com.demoStore.core.selenium.report;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

import br.com.demoStore.core.junit.OnError;
import br.com.demoStore.core.junit.Save;
import br.com.demoStore.core.selenium.report.exception.ReportException;

public class ReportPDF implements Report {

	private final String JAVA_SCRIPT_HIGHLITH =  "arguments[%d].setAttribute('style', 'border: 3px solid red!important');arguments[%d].focus();arguments[%d].scrollIntoView(true);";
	private final String JAVA_SCRIPT_UNDO = "arguments[%d].setAttribute('style', '%2$s');";
	private final float MARGIN = 20;
	
	private String path = "./Evidences/" + Thread.currentThread().getName()+".pdf";
	private PdfWriter writer;
	private Document document;
	private TakesScreenshot screenCapture;
	private JavascriptExecutor jse;	
	private Font tituloFormatacao;
	private Font subtituloFormatacao;
	private Font dadosFormatacao;
	private boolean textReport;
	private boolean discartStandardText;
	private String browserVersion; 
	private WebDriver webDriver;
	private ColumnText descText;
	
	public ReportPDF (WebDriver webDriver) {
		this.webDriver = webDriver;
		this.browserVersion = webDriver.toString().toLowerCase().contains("htmlunit")? "HtmlUnit"
				: ((RemoteWebDriver) webDriver).getCapabilities().getBrowserName().toLowerCase() + " " + 
				((RemoteWebDriver) webDriver).getCapabilities().getVersion().toString();
		this.screenCapture =  browserVersion.equals("HtmlUnit")? null : (TakesScreenshot)webDriver;
		this.jse = ((JavascriptExecutor)webDriver);
		this.document = new Document();		
		try {		
			Files.createDirectories(Paths.get(this.path).getParent());
			this.writer = PdfWriter.getInstance(document, new FileOutputStream(path));
		} 
		catch (FileNotFoundException e) {
			throw new ReportException(e);
		} 
		catch (DocumentException e) {
			throw new ReportException(e);
		} 
		catch (IOException e) {
			throw new ReportException ("Falha ao criar diretório de evidência. " + e.getMessage());
		}		
		this.document.open();

		PdfTemplate status = this.writer.getDirectContent().createTemplate(200, 200);
		this.descText = addTextArea (40, 30, 350, 150, status);
		try {
			this.descText.go();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		this.writer.getDirectContent().addTemplate(status, 0, 30);

		this.document.setMargins(MARGIN, MARGIN, MARGIN, MARGIN);
		this.tituloFormatacao = FontFactory.getFont(FontFactory.HELVETICA, 20, Font.BOLD);
		this.subtituloFormatacao = FontFactory.getFont(FontFactory.HELVETICA, 18, Font.BOLD, BaseColor.GRAY);
		this.dadosFormatacao = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL);
		this.textReport = true;
		this.discartStandardText = false;
	}
	
	public ReportPDF(WebDriver driver, String pathToSave) {
		this(driver);
		this.path = pathToSave;	
	}
	
	@Override
	public void setCover(String testName, String objective) {
		Paragraph titulo = new Paragraph(testName, tituloFormatacao);         
		try {
			
			Image imglogo = Image.getInstance("./resource/logo.jpg");
			Image imgcap = Image.getInstance("./resource/cap.png");
			imglogo.scaleToFit(125, 50);
			imgcap.setAbsolutePosition(300, 0);
			document.add(imglogo);
			document.add(imgcap);
			
			//Add Title
			ColumnText title = addTextArea(30, 400, 560, 720);			
			title.addText(titulo);			
			title.setLeading(23);
			title.go();
			     
			//Add objective
			ColumnText obj = addTextArea(30, 340, 560, 460);
			titulo = new Paragraph(70, objective, subtituloFormatacao);
			obj.addText(new Paragraph(titulo));
			obj.go();
			
	        String info = "Nome: Automação\nData: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())+
	        		"\nBrowser: " + browserVersion; 
	        
	        Paragraph p = new Paragraph(70, info);
	        this.descText.addText(p);
	        this.descText.go();
	        
	        document.newPage();
		} catch (BadElementException | IOException e) {
			throw new ReportException(e);
		} catch (DocumentException e) {
			throw new ReportException(e);
		}
	}
	
	@Override
	public void addStep() {
		addStep("");
	}
	
	@Override
	public void addStep(String description) {
			
		try {
        	if (document.isOpen()) {
	        	if(this.textReport){
	    			if(this.discartStandardText){
	    				EvidenceManager.clearTextReport(Thread.currentThread().getName());
	    				document.add(new Paragraph(description, dadosFormatacao));
	    			}
	    			else{
	        			StringBuilder report = new StringBuilder();
	        			for(String t : EvidenceManager.getTextReport(Thread.currentThread().getName())){
	        				report.append(t);
	        			}
	    				report.append("\n"+description);
	    				document.add(new Paragraph(report.toString(), dadosFormatacao));
	    			}
	    		}
	    		else{
	    			document.add(new Paragraph(description, dadosFormatacao)); 
	    		}
	  			byte[] pictureData = screenCapture.getScreenshotAs(OutputType.BYTES);
	  			setImage(pictureData);
        	}
		} catch (DocumentException e) {
			throw new ReportException(e);
		}finally{
			this.discartStandardText = false;
		}
	}
	
	@OnError
	@Override
	public void addStep(String description, boolean discartStandardText) {
		this.discartStandardText = discartStandardText;
		addStep(description);
	}
	
	@Override
	public void addStep(WebElement... webElement) {
		addStep("", webElement);	
	}
	
	@Override
	public void addStep(String description, WebElement... webElement) {
		
		if (webDriver.toString().toLowerCase().contains("phantom")){
			try {Thread.sleep(1000);} 
			catch (InterruptedException e) {e.printStackTrace();}
		}
		String javaScript ="";
		String initialStyle="";
		//Cria o javascript para desfazer
		for (int i = 0; i< webElement.length; i++) {
			initialStyle = initialStyle + String.format(this.JAVA_SCRIPT_UNDO, i ,webElement[i].getAttribute("style"));
		}
		//Cria o javascript de Highlight
		for (int i=0; i<webElement.length; i++) {
			javaScript = javaScript + String.format(JAVA_SCRIPT_HIGHLITH, i,i,i);
		}
		//Aguarda visibilidade de todos elementos
		waitVisibilityOfElement (webElement);
		//Executa o highlight em todos elementos				
		jse.executeScript(javaScript, (Object[])webElement);		
		addStep (description);
		jse.executeScript(initialStyle, (Object[])webElement);
	}
	
	
	
	@Override
	public void addFreeText(String description, String fontname, float size, int style, BaseColor color) {
		Font format;
		try {
			EvidenceManager.clearTextReport(Thread.currentThread().getName());
			format = FontFactory.getFont(fontname, size, style, color);
			document.add(new Paragraph( description, format));
		} catch (DocumentException e) {
			throw new ReportException(e);
		}
	}
	
	
	
	@Override
	public void addText(String description) {
		try {
			EvidenceManager.clearTextReport(Thread.currentThread().getName());
			document.add(new Paragraph( description, dadosFormatacao));
		} catch (DocumentException e) {
			throw new ReportException(e);
		}
	}
	
	private void setImage(byte [] pictureData){
	try {
		Image image = Image.getInstance(pictureData);
		image.scaleToFit(document.getPageSize().getWidth()-(MARGIN*2), document.getPageSize().getHeight());
		document.add(image);
		//document.newPage();
		} catch (BadElementException | IOException e) {
			throw new ReportException(e);
		} catch (DocumentException e) {
			throw new ReportException(e);
		}
	}
	
	@Override
	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public void save() {
		save (this.path);		
	}

	@Save
	@Override
	public void save (boolean isPassed) {
		save (this.path, isPassed);
	}
	
	@Override
	public void save(String path) {
		try {   			
			document.close();
        	writer.close();        	
			EvidenceManager.addEvidence(Paths.get(path));
		} catch (IOException e) {
			throw new ReportException(e);
		}
		
	}
	
	public void save (String path, boolean isPassed) {
		try {   			
			setStatus (isPassed);
			document.close();
        	writer.close();        	
			EvidenceManager.addEvidence(Paths.get(path));
		} catch (IOException e) {
			throw new ReportException(e);
		}
	}
	
	
	

	@Override
	public void disableStandardText() {
		this.textReport = false;
	}
	
	
	private void waitVisibilityOfElement (WebElement...elements) {
		WebDriverWait webDriverWait = new WebDriverWait (this.webDriver, 30);
		for (int i=0; i<elements.length; i++) {
			webDriverWait.until(ExpectedConditions.visibilityOf(elements[i]));
		}
	} 

	private ColumnText addTextArea (int startX, int endX, int startY, int endY, PdfContentByte contentByte) {
		ColumnText ct = new ColumnText (contentByte);
		ct.setSimpleColumn(startX, endX, startY, endY);
		return ct;
	}
	
	private ColumnText addTextArea (int startX, int endX, int startY, int endY) {
		return addTextArea (startX, endX, startY, endY, this.writer.getDirectContent());
	}
	
	
	private void setStatus (boolean isPassed) {
		if (isPassed) {			
			Font f = new Font ();
			f.setColor(BaseColor.GREEN);
			this.descText.addText(new Phrase ("Status: Passed", f));
		}
		else {
			Font f = new Font ();
			f.setColor(BaseColor.RED);
			this.descText.addText(new Phrase ("Status: Failed", f));
		}
		try {
			this.descText.go();
		}
		catch (DocumentException e) {
			e.printStackTrace();
		}
		
	}


}
