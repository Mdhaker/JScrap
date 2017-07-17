package utils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import config.Config;
import loaders.Loader;
import scrappers.Crawl;
import scrappers.Scraper;
import scrappers.Search;
import scrappers.Table;

public class Rinterface {
	
	private String url ;
	private Scraper scrapper ;
	private boolean selniuem = false ;
	public Rinterface(String url)
	{
		if(!url.contains("http"))
		{
			url = "http://"+url;
		}	
		this.url = url ;
		if(!url.isEmpty())
		{
		this.scrapper = Scraper.source(url);
		if(!this.scrapper.isJsoupLoaded())
			try
			{
				this.initSelniuemLoader();
				selniuem = true ;
			}
		catch(Exception e)
			{
				System.out.println(e.getMessage());
				System.out.println("Selniuem not working, this is the driver path "+Config.SELENIUM_CHROME_DRIVER_PATH);
				//this.initUnitLoader();
			}	
		}
	}
	public Rinterface()
	{
		
	}
	private void initSelniuemLoader()
	{
		this.scrapper = Scraper.source(Loader.getSeleniumLoader(url));
	}
	
	private void initUnitLoader()
	{
		this.scrapper = Scraper.source(Loader.getHtmlUnitLoader(url));
	}
	
	public String[] getEmails()
	{
		if(!Config.Debug)
		{
			Config.hideDebug();
		}
		Set<String> emails = this.scrapper.getEmails();
		Config.showDebug();
		return (String[]) emails.toArray(new String[emails.size()]);
	}
	
	public String[] getPhones()
	{
		if(!Config.Debug)
		{
			Config.hideDebug();
		}
		Set<String> phones = this.scrapper.getPhones();
		Config.showDebug();
		return (String[]) phones.toArray(new String[phones.size()]);
	}
	
	public String[] getExternalLinks()
	{
		if(!Config.Debug)
		{
			Config.hideDebug();
		}
		Set<String> externallinks = this.scrapper.getExternalLinks();
		Config.showDebug();
		return (String[]) externallinks.toArray(new String[externallinks.size()]);
	}
	
	public String[] getInternalLinks()
	{
		if(!Config.Debug)
		{
			Config.hideDebug();
		}
		Set<String> internalLinks = this.scrapper.getInternalLinks();
		Config.showDebug();
		return (String[]) internalLinks.toArray(new String[internalLinks.size()]);
	}
	
	public String[] getMedia(String media)
	{
		if(!Config.Debug)
		{
			Config.hideDebug();
		}
		Config.showDebug();
		List<String> medias = this.scrapper.getMedias().get(media);
		return (String[]) medias.toArray(new String[medias.size()]);
	}
	
	public String[] getSearchKeyResult(String key)
	{
		if(!Config.Debug)
		{
			Config.hideDebug();
		}
		Set<String> keys = Search.source(this.scrapper.getRootDocument()).find(key);
		Config.showDebug();
		return (String[]) keys.toArray(new String[keys.size()]);
	}
	
		public String[] getSocialLinks()
	{
			if(!Config.Debug)
			{
				Config.hideDebug();
			}
		Set<String> socialLinks = this.scrapper.getSocialMediaLinks();
		Config.showDebug();
		return (String[]) socialLinks.toArray(new String[socialLinks.size()]);
	}
	
	public static void setChromeDriverPath(String path)
	{
		Config.SELENIUM_CHROME_DRIVER_PATH=path+Config.SELENIUM_CHROME_DRIVER_PATH;
	}
	
	public void scrapInFile(String path)
	{
		if(!(path.endsWith(".xls")||path.endsWith(".xlsx")))
		{
			System.err.println("Please provide an excel file path ending with ex file.xls or file.xlsx");
		}
		if(!Config.Debug)
		{
			Config.hideDebug();
		}
		System.out.println("This is selniuem path : "+Config.SELENIUM_CHROME_DRIVER_PATH);
		System.out.println("Scraping started");
		ExcelWrite writer = new ExcelWrite(path);
		if(!selniuem)
			this.initSelniuemLoader();
		while(!this.scrapper.isLastPage())
		{
			System.out.println("Selniuem started...");
		writer.saveScrapingResult(this.scrapper);
			for(Table table : this.scrapper.getTables())
			{
				writer.saveTable(table);
			}
			this.scrapper.nextPage();
			writer.createSheet("page "+this.scrapper.getCurrentPage());
		}
		Config.showDebug();
			
	}
	
	/**
	 * Download all images
	 * @param path
	 */
	public void downloadImages(String path)
	{
		if(!Config.Debug)
		{
			Config.hideDebug();
		}
		Download.mkdir(path);
		Scraper scraper = Scraper.source(this.url);
		for(String image :scraper.getMedias().get("image"))
		{
			String filename=image.substring(image.lastIndexOf("/")+1);
			
			for(String ext : scraper.getImagesExt())
			{
				if(filename.endsWith("."+ext))
				{
					
					try {
						Download.saveFileFromUrlWithCommonsIO("/"+path+filename, image);
					} catch (MalformedURLException e) {
						System.out.println(e.getMessage());
					} catch (IOException e) {
						System.out.println(e.getMessage());
					}
				}
			}
		}
		Config.showDebug();
		
	}
	/**
	 * Download all documents
	 * @param path
	 */
	public void downloadDocuments(String path)
	{
		if(!Config.Debug)
		{
			Config.hideDebug();
		}
		Download.mkdir(path);
		Scraper scraper = Scraper.source(this.url);
		for(String doc :scraper.getMedias().get("document"))
		{
			String filename=doc.substring(doc.lastIndexOf("/")+1);
			
			for(String ext : scraper.getDocumentExt())
			{
				if(filename.endsWith("."+ext))
				{
					try {
						Download.saveFileFromUrlWithJavaIO("/"+path+filename, doc);
					} catch (MalformedURLException e) {
						System.out.println(e.getMessage());
					} catch (IOException e) {
						System.out.println(e.getMessage());
					}
				}
			}
		}
		Config.showDebug();
	}	
	
	/**
	 * Download all video
	 * @param path
	 */
	public void downloadVideos(String path)
	{
		if(!Config.Debug)
		{
			Config.hideDebug();
		}
		Download.mkdir(path);
		Scraper scraper = Scraper.source(this.url);
		for(String doc :scraper.getMedias().get("video"))
		{
			String filename=doc.substring(doc.lastIndexOf("/")+1);
			
			for(String ext : scraper.getDocumentExt())
			{
				if(filename.endsWith("."+ext))
					{
						try {
							Download.saveFileFromUrlWithJavaIO("/"+path+filename, doc);
						} catch (MalformedURLException e) {
							System.out.println(e.getMessage());
						} catch (IOException e) {
							System.out.println(e.getMessage());
						}
					}
			}
		}
		Config.showDebug();
	}
	/**
	 * Saving result of a keyword search
	 * @param path
	 * @param key
	 */
	
	public void saveSearch(String path,String key)
	{
		if(!Config.Debug)
		{
			Config.hideDebug();
		}
		ExcelWrite write = new ExcelWrite(path);
		write.saveSearchResult(this.url, key);
		Config.showDebug();
	}
	
	public void crawlSite(String excelpath,String downloadpath)
	{
		if(!Config.Debug)
		{
			Config.hideDebug();
		}
		ExcelWrite write = new ExcelWrite(excelpath);
		Crawl.build(this.url).withSave(write).withDownload(downloadpath).start();
		Config.showDebug();
	}
	
	public void downloadFromFreeSound(String path,String keyword,String... loginpage)
	{
		if(!Config.Debug)
		{
			Config.hideDebug();
		}
		String rootURL ="https://www.freesound.org/search";
		String url =rootURL+"?q="+keyword;
		url=url+"&f=duration:[15 TO *]";
		System.out.println("The url to fetch : "+url);
		setChromeDriverPath("/home/dhaker/datacollector/");
		Scraper freesound = null;
		if(loginpage[0] !=null)
		{
			freesound = Scraper.source(Loader.getSeleniumLoader(loginpage[0]));
			if(loginpage[1] != null&&loginpage[2] !=null)
			{
				freesound.updateContent(freesound.getLoader().login(loginpage[1], loginpage[2], url));
			}
			else
				System.err.println("No login or password provided for login");
		}
		
		
		Set<String> downloadPages = new HashSet<String>();
		while(!freesound.isLastPage())
		{
			Elements links = freesound.getHTMLelements("div.sound_filename > a.title");
			//System.out.println("link elements found "+links.size());
			for(Element link:links)
			{
				System.out.println(link.text());
				downloadPages.add((Utility.formatHTTPS(freesound.getBaseURL()+link.attr("href"))));
			}
			freesound = freesound.nextPage();
		}
		List<String> pageurls = new ArrayList<String>();
		List<String> titles = new ArrayList<String>();
		List<String> authors = new ArrayList<String>();
		List<String> description = new ArrayList<String>();
		List<String> downloads = new ArrayList<String>();
		List<String> dates = new ArrayList<String>();
		List<String> tags = new ArrayList<String>();
		List<String> type = new ArrayList<String>();
		List<String> duration = new ArrayList<String>();
		List<String> fileSize = new ArrayList<String>();
		List<String> licence = new ArrayList<String>();
		List<String> licenceDescription = new ArrayList<String>();
		for(String page : downloadPages)
		{
			//System.out.println("Page : "+page);
			Scraper dpscrapper = Scraper.source(Jsoup.parse(freesound.getLoader().navigateToUrl(page, false))).downloadMedias("audio", path);
			titles.add(dpscrapper.selectElements("#single_sample_header > a").text());
			pageurls.add(page);
			authors.add(dpscrapper.selectElements("#sound_author > a").text());
			licenceDescription.add(dpscrapper.selectElements("#sound_license > a").text());
			licence.add(dpscrapper.selectElements("#sound_license > a").attr("href"));
			dates.add(dpscrapper.selectElements("div #sound_date").text());
			tags.add(dpscrapper.selectElements("#single_sample_content > ul > li > a").text());
			description.add(dpscrapper.selectElements("#sound_description").text());
			downloads.add(dpscrapper.selectElements("#download_text > a > b").text());		
			type.add(dpscrapper.selectElements("#sound_information_box > dd:nth-child(2)").text());
			duration.add(dpscrapper.selectElements("#sound_information_box > dd:nth-child(4)").text());
			fileSize.add(dpscrapper.selectElements("#sound_information_box > dd:nth-child(6)").text());
		}
		ExcelWrite writer = new ExcelWrite(path+"/freesound.xls");
		writer.addColumn(pageurls, "Url").addColumn(titles,"Title").addColumn(authors,"Author").addColumn(dates,"Date")
		.addColumn(downloads, "Downloaded")
		.addColumn(description,"Description")
		.addColumn(tags,"Tags")
		.addColumn(type,"Types")
		.addColumn(duration,"Duration")
		.addColumn(fileSize,"File size")
		.addColumn(licence,"Licence")
		.addColumn(licenceDescription,"Licence description")		;
		Config.showDebug();
	}
	
	
	

}
