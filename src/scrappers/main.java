package scrappers;

import org.apache.commons.exec.OS;

import config.Config;
import loaders.HtmlUnitLoader;
import loaders.Loader;
import utils.ExcelWrite;
import utils.Rinterface;

public class main {

	public static void main(String[] args) 
	{ 
		Rinterface.setChromeDriverPath("/home/dhaker/datacollector/inst");
		Rinterface r = new Rinterface("https://www.brainyquote.com/quotes/keywords/scrap.html");
		
		String path ="/home/dhaker/Desktop/ThisDirectoryForTest/";
		System.out.println(Scraper.source(Loader.getSeleniumLoader("https://www.brainyquote.com/quotes/keywords/scrap.html")).getEmails());
	}
}
