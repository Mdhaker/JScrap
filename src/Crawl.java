import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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
		
		//Recursive call starts
		System.out.println("Started Crawling...");

		this.crawling(url);
		System.out.println("\nFinished Crawling and will start "+this.scrapers.size()+" threads..."+
		" while must start "+this.visitedLinks.size()+" threads ?");
		for(Scraper scraper:this.scrapers)
			{
				System.out.println("\nCreating scrapper robot with this url : "+scraper.getRootURL());
				Robot robot = new Robot(scraper);
				robot.start();
			}
	}
	/**
	 * Recursive method to crawl all link from a given link
	 * @param url root link
	 */
	private void crawling(String url)
	{
		if (this.visitedLinks.contains(url))
			return;
		this.visitedLinks.add(url);
		Scraper currentScraper = Scraper.source(url);
		System.out.println("\nThis is the current url "+url);
		this.scrapers.add(currentScraper);
		System.out.println("\nNumber of created scrappers ="+this.scrapers.size());
		System.out.println("\nNumber of visited links ="+this.visitedLinks.size());
		Iterator<String> internalLinksIterator = currentScraper.getInternalLinks().iterator();
		while(internalLinksIterator.hasNext())
			{
				String link = internalLinksIterator.next();
				crawling(link);
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


