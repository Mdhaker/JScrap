
public class main {

	public static void main(String[] args) 
	{ 
		String url = "https://transtats.bts.gov/DataIndex.asp";
			
		Scraper.getInstance().addImagesExt("png");
		Scraper.getInstance().addImagesExt("gif");
		Scraper.getInstance().addDocumentExt("js");
		System.out.println(Scraper.source(url).getMedias());
		
    }

}
