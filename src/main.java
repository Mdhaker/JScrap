
public class main {

	public static void main(String[] args) 
	{ 
		String url = "https://www.randomlists.com/phone-numbers";
		
		
		//System.out.println(Scraper.source(url).getPhones().toString());
		System.out.print(Search.source(url).find("phone"));
		
		
    }

}
