
public class main {

	public static void main(String[] args) 
	{ 
		String url = "http://www.deltagroup.com.tn/contactez-nous/";
		
		//System.out.println(Scraper.source(url).startCrawl());
		//System.out.println(Scraper.source(url).startCrawl().getEmails());
		System.out.println(Search.source(url).find("Coordonnées"));
		
		
    }

}
