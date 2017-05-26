package utils;

import scrappers.Scraper;

public class Rinterface {
	
	private String url ;
	public Rinterface(String url)
	{
		this.url = url ;
	}
	
	public void scrapInFile(String path)
	{
		ExcelWrite writer = new ExcelWrite(path);
		writer.saveScrapingResult(Scraper.source(url));
	}
	

}
