import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTMLextractor {
	
	private Set<String> images;
	private Set<String> emails;
	private Set<String> phones;
	private Set<String> Links;
	private Set<String> SocialLinks;
	
	private static Document htmlpage ;
	private static String baseURL ;
	private static HTMLextractor instance = new HTMLextractor();
	
	// This is the constructor method for loading the page
	public static HTMLextractor source(String url)
	{	
		try 
		{
			htmlpage = Jsoup.connect(url).get();
			return instance;			
		} 
		
		catch (Exception e) 
		{	System.out.println(e.getMessage());
			return instance ;
		}
	}
	
	
	// Fetching all images in the page
	
    public Set<String> getimages() 
    {
       this.images = new HashSet<String>();
       Elements imgs = htmlpage.select("[src]");
       
		Pattern imgPattern = Pattern.compile("(.*/)*.+\\.(png|jpg|gif|bmp|jpeg|PNG|JPG|GIF|BMP)$");
       int i = 0;
       for (Element img : imgs) 
       {   
    	   		if(imgPattern.matcher(img.attr("abs:src")).find())
    	   			this.images.add(img.attr("abs:src"));
       }
		return images;
	}

    // Getting emails with a regex
    
	public Set<String> getEmails() 
	{
		this.emails = new HashSet<String>();
		Pattern mailPattern = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+");
        Matcher matcher = mailPattern.matcher(htmlpage.text());
        while (matcher.find()) 
        {
            emails.add(matcher.group());
        }
		return emails;
	}

	public Set<String> getPhones() 
	{
		this.phones = new HashSet<String>();
		return phones;
	}
	public Set<String> getLinks() 
	{
		this.Links = new HashSet<String>();
		return Links;
	}
	public Set<String> getSocialLinks() 
	{
		this.SocialLinks = new HashSet<String>();
		return SocialLinks;
	}


	public static void main(String[] args) 
	{       
       System.out.println(HTMLextractor.source("https://www.zaubacorp.com/company/DELTA-SOFT-SOLUTIONS-INDIA-PRIVATE-LIMITED/U72200TG2007PTC055389").getimages().size());
    }

   
}