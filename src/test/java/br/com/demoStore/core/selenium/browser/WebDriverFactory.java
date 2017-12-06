package br.com.demoStore.core.selenium.browser;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import br.com.demoStore.core.selenium.browser.exception.BrowserException;

/**
 * 
 * Name: {@link WebDriverFactory}

 */
public class WebDriverFactory {
	
	final static Logger logger = Logger.getLogger(WebDriverFactory.class);
	
	final static String INTERNET_EXPLORER = "internetexplorer";
	
	final static String FIREFOX = "firefox";
	
	final static String CHROME = "chrome";
	

	
	/**
	 * Construtor vazio
	 */
	private WebDriverFactory() {
	}
	
	/**
	 * Obter o web driver (IE, Firefox, Chrome)
	 * @param browserType
	 * @param addressSelenium
	 * @return
	 * @throws Exception
	 */
	public static WebDriver getWebDriver(BrowserType browserType, String addressSelenium ) throws Exception {
		return getWebDriver(browserType.toString(), addressSelenium);
	}
	
	/**
	 * Obter tipo do browser informado pelo usuário
	 * @param BrowserType
	 * @param addressSelenium
	 * @return
	 * @throws Exception
	 */
	public static WebDriver getWebDriver(String browserType, String addressSelenium ) throws Exception {
		
		if(!addressSelenium.toLowerCase().contains("localhost")){
			return getRemoteWebDriver(browserType, addressSelenium);
		}
		
		switch (browserType.toLowerCase()) {
	    	case INTERNET_EXPLORER:
	    		return getInternetExplorerDriver(); 
		 	case FIREFOX:
		    	return getFirefoxDriver();    		    
		    case CHROME:
		    	return getChromeDriver(); 
		  
		    default:
		    	String e = "Não foi informado browser suportado pela aplicação";
				throw new BrowserException(e);
		}
	}
	
	/**
	 * Obter driver do Internet Explorer
	 * @return
	 */
	private static WebDriver getInternetExplorerDriver() {
		System.setProperty("webdriver.ie.driver", "./resource/drivers/IEDriverServer.exe");
		WebDriver webDriver = new InternetExplorerDriver(getCapabilitiesInternetExplorerDriver());
		webDriver.manage().window().maximize();
		logger.info("Iniciado Browser Internet Explorer");
		return webDriver;
	}
	
	/**
	 * Obter driver do Firefox
	 * @return
	 */
	private static WebDriver getFirefoxDriver(){
		WebDriver webDriver= new FirefoxDriver(getCapabilitiesFirefoxDriver());
		webDriver.manage().window().maximize();
		logger.info("Iniciado Browser FireFox");
		return webDriver;
	}
	
	/**
	 * Obter driver do Chrome
	 * @return
	 */
	private static WebDriver getChromeDriver() {
		System.setProperty("webdriver.chrome.driver", "./resource/drivers/chromedriver.exe");
		WebDriver webDriver = new ChromeDriver(getCapabilitiesChromeDriver());
		webDriver.manage().window().maximize();
		logger.info("Iniciado Browser Chrome");
		return webDriver;
	}
	
	
	/**
	 * Obter capabilities para o internet explorer
	 * @return
	 */
	private static DesiredCapabilities getCapabilitiesInternetExplorerDriver() {
		DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
		capabilities.setCapability(InternetExplorerDriver.INITIAL_BROWSER_URL, "about:blank");
		capabilities.setCapability(InternetExplorerDriver.NATIVE_EVENTS, false);
		capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
		capabilities.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
		capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		capabilities.setCapability("javascriptEnabled", true);
		return capabilities;
	}
	
	/**
	 * Obter capabilities para o firefox
	 * @return
	 */
	private static DesiredCapabilities getCapabilitiesFirefoxDriver(){
		FirefoxProfile profile = new FirefoxProfile();
		DesiredCapabilities capabilities = DesiredCapabilities.firefox();
		profile.setPreference("browser.startup.homepage", "about:blank");
		profile.setPreference("startup.homepage_welcome_url", "about:blank");
		profile.setPreference("startup.homepage_welcome_url.additional", "about:blank");
		profile.setPreference("browser.startup.homepage_override.mstone","ignore");
		/*Gerenciamento de Download*/
		String path =  "./resource/files/Massa";
		profile.setPreference("browser.download.useDownloadDir", true);
		profile.setPreference("browser.download.dir", path);
		profile.setPreference("browser.helperApps.neverAsk.saveToDisk", "application/csv");
		profile.setPreference("browser.download.folderList", 2);
		profile.setPreference("browser.download.manager.alertOnEXEOpen", false);
		profile.setPreference("browser.helperApps.neverAsk.saveToDisk", "application/msexcel, application/msword, application/csv, application/ris, text/csv, image/png, application/pdf, text/html, text/plain, application/zip, application/x-zip, application/x-zip-compressed, application/download, application/octet-stream");
		profile.setPreference("browser.download.manager.showWhenStarting", false);
		profile.setPreference("browser.download.manager.focusWhenStarting", false);  
		profile.setPreference("browser.helperApps.alwaysAsk.force", false);
		profile.setPreference("browser.download.manager.alertOnEXEOpen", false);
		profile.setPreference("browser.download.manager.closeWhenDone", true);
		profile.setPreference("browser.download.manager.showAlertOnComplete", false);
		profile.setPreference("browser.download.manager.useWindow", false);
		profile.setPreference("services.sync.prefs.sync.browser.download.manager.showWhenStarting", false);
		/*Gerenciamento de Download*/
		profile.setEnableNativeEvents(true);
		capabilities.setCapability(FirefoxDriver.PROFILE, profile);
		capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		return capabilities;
	}
	
	/**
	 * Obter capabilities para o chrome
	 * @return
	 */
	private static DesiredCapabilities getCapabilitiesChromeDriver() {
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();

        Map<String, Object> preferences = new Hashtable<String, Object>();
        preferences.put("profile.default_content_settings.popups", 0);
        preferences.put("download.prompt_for_download", "false");
        preferences.put("download.default_directory", "./resource/files/Massa");

        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", preferences);

        capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
       
        return capabilities;
  }
	
	/**
	 * Carregar web driver remoto
	 * @param BrowserType
	 * @param addressSelenium
	 * @return
	 * @throws Exception
	 */
	private static WebDriver getRemoteWebDriver(String BrowserType, String addressSelenium) throws Exception{
		try {
			switch (BrowserType.toLowerCase()) {
	    	case INTERNET_EXPLORER:
	    		return new RemoteWebDriver(new URL("http://" + addressSelenium + ":4444/wd/hub"),getCapabilitiesInternetExplorerDriver()); 
		 	case FIREFOX:
		    	return new RemoteWebDriver(new URL("http://" + addressSelenium + ":4444/wd/hub"),getCapabilitiesFirefoxDriver());  		    
		    case CHROME:
		    	return new RemoteWebDriver(new URL("http://" + addressSelenium + ":4444/wd/hub"),getCapabilitiesChromeDriver()); 
		  
		    default:
		    	String e = "Não foi informado browser suportado pela aplicação";
				throw new BrowserException (e);
			}
		} catch (MalformedURLException e) {
			throw new BrowserException("Falha ao tentar iniciar o RemoteWebDriver para o endereço " + addressSelenium);
		}
	}
	
	/**
	 * Troca contexto do webdriver
	 * @param webDriver
	 * @param browserType
	 * @param addressSelenium
	 * @return
	 * @throws Exception
	 */
	public static WebDriver changeWebDriver(WebDriver webDriver, BrowserType browserType, String addressSelenium) throws Exception{
		webDriver.close();
		webDriver.quit();
		return getWebDriver(browserType,addressSelenium);
	}
	
}
