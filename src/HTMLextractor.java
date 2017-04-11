import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTMLextractor {
	
	private Set<String> images;
	private Map<String,String> medias;
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
		System.out.println(url.split("^.+?[^\\/:](?=[?\\/]|$)")[0].toString());
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
       Pattern imgPattern = Pattern.compile("(.*/)*.+\\.(png|jpg|gif|bmp|jpeg|PNG|JPG|GIF|BMP)$");
       int i = 0;
       Elements imgs = htmlpage.select("[src]");
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

		// getting phone numbers via a regex
	public Set<String> getPhones() 
	{
		this.phones = new HashSet<String>();
		Pattern phonePattern = Pattern.compile("(\\d[.-])?\\(?\\d{3}\\)?[-. ]?\\d{3}[-. ]?\\d{4}\\b");
        Matcher matcher = phonePattern.matcher(htmlpage.text());
        System.out.println(matcher.find());
        while (matcher.find()) 
        {
            phones.add(matcher.group());
        }
		
		return phones;
	}
	// getting href links 
	public Set<String> getLinks() 
	{
		this.Links = new HashSet<String>();
		Elements lnks = htmlpage.select("a[href]");
		 for (Element link : lnks) 
		 {
			 this.Links.add(link.attr("abs:href"));
		 }
		return Links;
	}
	//getting social media links
	public Set<String> getSocialLinks() 
	{
		this.SocialLinks = new HashSet<String>();
		return SocialLinks;
	}


	public static void main(String[] args) 
	{       
       System.out.println(HTMLextractor.source("https://www.ssa.gov/agency/contact/phone.html").getPhones().toString());
    }

   
}