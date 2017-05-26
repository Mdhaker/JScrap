package scrappers;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import utils.Download;
import utils.ExcelWrite;

/**
 * 
 * @author MDA
 *
 */
public class Crawl 
{
	private String rootURL ;
	private Set<String> visitedLinks;
	private Set<Scraper> scrapers ;
	private Set<String> emails,phones;
	private ExcelWrite writer=null;
	private boolean download=false;
	private String downloadPath;
	private String[] mediaFilterKeys;
	
	public static Crawl build(String url)
	{
		return new Crawl(url);
	}
		
	private Crawl(String url)
	{
		this.rootURL = url;
		this.scrapers = new HashSet<Scraper>();
		this.visitedLinks = new HashSet<String>();
		this.emails = new HashSet<String>();
		this.phones = new HashSet<String>();

	}
	/**
	 * start recurisve crawling
	 * @return
	 */
	
	public Crawl start()
	{
		//Recursive call starts
				System.out.println("Started Crawling...");
				this.crawling(this.rootURL,this.writer);
				System.out.println("\nFinished Crawling and will start "+this.scrapers.size()+" threads..."+
				" while must start "+this.visitedLinks.size()+" threads ?");
				for(Scraper scraper:this.scrapers)
					{
						System.out.println("\nCreating scrapper robot with this url : "+scraper.getRootURL());
						Robot robot = new Robot(scraper);
						robot.start();
					}
		return this;
	}
	
	public Crawl withSave(ExcelWrite writer)
	{
		this.writer = writer ;
		return this;
	}
	/**
	 * enable download of medias
	 * @param path of the download
	 * @return
	 */
	public Crawl withDownload(String path)
	{
		Download.mkdir(path);
		this.download = true ;
		this.downloadPath = path ;
		return this ;
	}
	/**
	 * Filter medias files with key strings
	 * @param strings
	 * @return
	 */
	public Crawl withMediaFilter(String...strings)
	{
		this.mediaFilterKeys = strings;
		return this ;
	}
	/**
	 * Recursive method to crawl all link from a given link
	 * @param url root link
	 */
	private int page = 0;
	private void crawling(String url,ExcelWrite writer)
	{
		if (this.visitedLinks.contains(url))
			return;
		if(writer != null)
		{
			writer.createSheet("sheet for link number "+page);
			page++;
			Scraper.source(url).saveToexcel(writer);
		}
		this.visitedLinks.add(url);
		Scraper currentScraper = Scraper.source(url);
		System.out.println("\nThis is the current url "+url);
		this.scrapers.add(currentScraper);
		System.out.println("\nNumber of created scrappers ="+this.scrapers.size());
		System.out.println("\nNumber of visited links ="+this.visitedLinks.size());
		if(this.download)
		{
			if(this.mediaFilterKeys != null)				
				currentScraper.withMediaFilter(this.mediaFilterKeys).downloadMedias("image", this.downloadPath+"images/");
			else
				currentScraper.downloadMedias("image", this.downloadPath+"images/");
		}
		Iterator<String> internalLinksIterator = currentScraper.getInternalLinks().iterator();
		while(internalLinksIterator.hasNext())
			{
				String link = internalLinksIterator.next();
				crawling(link,writer);
			}		
	}
	public Set<String> getEmails() 
	{
		return emails;
	}
	
	public synchronized void addEmails(Set<String> emails) 
	{
		this.emails.addAll(emails);
	}
	public Set<Scraper> getScrapers() 
	{
		return scrapers;
	}
	/**
	 * @author MDA
	 */
	private static int count=0;
	private class Robot extends Thread
	{
		private Set<Scraper> scrapers;
		public Robot(Scraper... scrapers)
		{
			super();
			this.scrapers = new HashSet<Scraper>();
			for(Scraper scraper: scrapers)
				this.scrapers.add(scraper);
			count++;
		}
		@Override
		public void run() 
		{	super.run();
			for(Scraper scraper:this.scrapers)
			{
				//System.out.println("Current thread "+ count +" url"+url);
				addEmails(scraper.getEmails());
			}			
		}
		
	}
}


