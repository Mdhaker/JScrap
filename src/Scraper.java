import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Scraper {
	
	private Set<String> images;
	private Map<String,List<String>> medias;
	private Set<String> emails;
	private Set<String> phones;
	private Set<String> Links;
	private Set<String> externalLinks;
	private Set<String> internalLinks;
	private Set<String> SocialLinks;
	private static Document htmlpage ;
	private static String baseURL ;
	private Vector<String> videoExt ;
	private Vector<String> audioExt ;
	private Vector<String> imagesExt ;
	private Vector<String> documentExt ;
	private Vector<String> iframes ;
	private static Scraper instance = new Scraper();
	
	public static Scraper getInstance()
	{
		return instance ;
	}
	private Scraper()
	{
		this.videoExt = new Vector<String>();
		this.videoExt.add("mpeg");this.videoExt.add("ogg");this.videoExt.add("flv");this.videoExt.add("mp4");
		this.imagesExt = new Vector<String>();
		this.documentExt = new Vector<String>();
		this.audioExt = new Vector<String>();
		this.iframes = new Vector<String>();
	}
	// This is the constructor method for loading the page
	public static Scraper source(String url)
	{	
		baseURL = url.split("://")[1].split(":")[0].split("/")[0];
		System.out.println("base url : "+baseURL);
		try 
		{
			htmlpage = Jsoup.connect(url).get();
			if(!htmlpage.select("iframe").isEmpty())
				instance.iframes.addElement(htmlpage.select("iframe").iterator().next().attr("src"));
			return instance;			
		}
		catch (Exception e) 
		{	System.out.println(e.getMessage());
			e.printStackTrace();
			return instance ;
		}
	}
	// Fetching all images in the page
	/**
	 * @deprecated  we use get media to get all type of media including images </br>
	 *              
	 * use {@link #getMedia()} instead like this: 
	 * 
	 * <blockquote>
	 * <pre>
	 * getMedia().get("image")
	 * </pre></blockquote>
	 * 
	 */
	@Deprecated
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
        if(this.iframes.isEmpty())
        	return emails;
        else
        {        	
        	emails.addAll(Scraper.source(this.iframes.iterator().next()).getEmails());
        	return emails ;
        }
	}
		// getting phone numbers via a regex
	public Set<String> getPhones() 
	{
		this.phones = new HashSet<String>();
		Pattern phonePattern = Pattern.compile("(\\d[.-])?\\(?\\d{3}\\)?(\\s)*[-.( ]?\\d{3}[-.) ]?(\\s)*\\d{4}\\b");
        Matcher phoneMatcher = phonePattern.matcher(htmlpage.text());
        System.out.println(phoneMatcher.find());
        while (phoneMatcher.find()) 
        {
            phones.add(phoneMatcher.group());
        }
		
        if(this.iframes.isEmpty())
        	return phones;
        else
        {        	
        	phones.addAll(Scraper.source(this.iframes.iterator().next()).getPhones());
        	return phones ;
        }
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
		 this.fetchLinks();
		
	        	return Links;
	}
	/**
	 * @return array of links wanted
	 */
	//getting social media links
	public Set<String> getSocialLinks(String socialmedia) 
	{
		this.SocialLinks = new HashSet<String>();
		Pattern socialPattern =Pattern.compile("http(s)?:\\/\\/(www\\.)?("+socialmedia+")\\.com\\/(A-z 0-9 _ - \\.)\\/?"); 
		Matcher socialMatcher = socialPattern.matcher(htmlpage.text());
		System.out.print("is there any "+socialmedia+" links found in this page :" +socialMatcher.find());
		while(socialMatcher.find())
		{
			this.SocialLinks.add(socialMatcher.group());
		}
		return SocialLinks;
	}
	
	public Set<String> getExternalLinks() 
	{
		if(this.Links == null)
			{
				this.getLinks();
			}
			return externalLinks;
	}
	public Set<String> getInternalLinks() 
	{
		if(this.Links == null)
			this.getLinks();
		return internalLinks;
	}
	// get file by ext type
	
	public Set<String> getFiles(String ext)
	{
		HashSet<String> fetchedFiles = new HashSet<String>();
		Elements files = htmlpage.select("[src]");
		for (Element file : files) 
	    {
	    	Matcher m = Pattern.compile("[^/.]*\\.(\\w+)[^/]*$").matcher(file.attr("abs:src"));
	    	System.out.println(file.attr("abs:src"));
	    	if(m.find()&&m.group(1).equals(ext))
	    	{
	    		fetchedFiles.add(file.attr("abs:src"));		
	    	}
				
	 	}
		return fetchedFiles;
	}
	/**
	 * Exctract all media types by their type
	 * @param ext Media extension to extract.
	 */
	public Map<String,List<String>> getMedias()
	{
		this.medias = new HashMap<String,List<String>>();
		List<String> audioMedia = new ArrayList<String>();
		List<String> imageMedia = new ArrayList<String>();
		List<String> documentMedia = new ArrayList<String>();
		List<String> videoMedia = new ArrayList<String>();
		List<String> unknownMedia = new ArrayList<String>();
	    Elements medias = htmlpage.select("[src]");
	    String ext="";
	    for (Element meds : medias) 
	    {
	    	System.out.println(meds.attr("abs:src"));
	    	Matcher m = Pattern.compile("[^/.]*\\.(\\w+)[^/]*$").matcher(meds.attr("abs:src"));
	    	if(m.find())
	    	{
	    		ext=m.group(1);
	    	}
				if(this.imagesExt.contains(ext))
				{
					imageMedia.add(meds.attr("abs:src"));
				}
				else if(this.documentExt.contains(ext))
				{
					documentMedia.add(meds.attr("abs:src"));
				}
				else if (this.videoExt.contains(ext))
				{
					videoMedia.add(meds.attr("abs:src"));
				}
				else if (this.audioExt.contains(ext))
				{
					audioMedia.add(meds.attr("abs:src"));
				}
				else
					unknownMedia.add(meds.attr("abs:src"));
	 	   
				this.medias.put("image", imageMedia);
				this.medias.put("video", videoMedia);
				this.medias.put("document", documentMedia);
				this.medias.put("audio", audioMedia);
	 	   		this.medias.put("unknown", unknownMedia);
	 	}
		return this.medias;
	}
	
	private Vector<String> getVideoExt() 
	{
		return videoExt;
	}
	public void addVideoExt(String videoExt) 
	{
		this.videoExt.add(videoExt);
	}
	private Vector<String> getImagesExt() 
	{
		return imagesExt;
	}
	public void addImagesExt(String imagesExt) 
	{
		this.imagesExt.add(imagesExt);
	}
	private Vector<String> getDocumentExt() 
	{
		return documentExt;
	}
	public void addDocumentExt(String documentExt) 
	{
		this.documentExt.add(documentExt);
	}
	
	private Vector<String> getAudioExt() {
		return audioExt;
	}
	public void addAudioExt(String audioExt) 
	{
		this.audioExt.add(audioExt);
	}
	
	
	// Fetching internal and external links by the base url and redirect words...
	private void fetchLinks()
	{
		this.externalLinks = new HashSet<String>();
		this.internalLinks = new HashSet<String>();
		for(String link : this.Links)
		{
			if(link.contains(baseURL)
					&&!link.contains("URL=")
					&&!link.contains("redirect")
					&&!link.split("://")[1].split(":")[0].split("/")[1].contains("http"))
			{
				this.internalLinks.add(link);
			}
			else
			{
				this.externalLinks.add(link);
			}
		}
	}
	// get HTML table
	
	public Set<Table> getTables()
	{
		HashSet<Table> tables = new HashSet<Table>();
		Elements tableElements = htmlpage.select("table");
		
		for(Element tab : tableElements)
		{
			tables.add(Table.parse(tab));
		}
		return tables ;
	}
	
	// Recursion calls for the hole site
	
	public void getAllEmails(Set<String> links)
	{
		
	}


	

   
}