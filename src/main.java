
public class main {

	public static void main(String[] args) 
	{ 
		String url = "http://www.solaraccreditation.com.au/products/inverters/approved-inverters.html";
			
		System.out.println(Scraper.source(url).getTables().iterator().next().getValueAt(0, 1));
		
		
    }

}
