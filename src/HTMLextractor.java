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
	
	public static Document htmlpage ;
	public static HTMLextractor source(String url)
	{
		try 
		{
			htmlpage = Jsoup.connect(url).get();
			return new HTMLextractor();
			
		} 
		
		catch (IOException e) 
		{			
			return null ;
		}
	}
	
	
	
    public Set<String> getImages() 
    {
       this.images = new HashSet<String>();
		return images;
	}

    // Getting emails with a regex
    
	public Set<String> getEmails() 
	{
		this.emails = new HashSet<String>();
		Pattern p = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+");
        Matcher matcher = p.matcher(htmlpage.text());
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
		return Links;
	}
	public Set<String> getSocialLinks() 
	{
		return SocialLinks;
	}


	public static void main(String[] args) throws IOException {
        String url = "http://www.orchardford.com/pre-owned/ford/fusion/2009-blue-sel-4947700.html";
        System.out.println("Fetching..."+url);

        Document doc = Jsoup.connect(url).get();
        Elements media = doc.select("img[src]");
     
       
    }

   
}