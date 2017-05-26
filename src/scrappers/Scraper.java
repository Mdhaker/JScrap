package scrappers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import loaders.Loader;
import utils.Download;
import utils.ExcelWrite;
import utils.Filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * 
 * @author MDA
 *
 */
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
	private static Scraper instance;
	private String rootURL;
	private String[] mediaFilterKeys;
	private Loader loader ;
	
	private Scraper()
	{
		this.videoExt = new Vector<String>();
		this.addVideoExt("mpeg","mp4","flv","swf","avi");
		this.imagesExt = new Vector<String>();
		this.addImageExt("png","jpg","jpeg","tiff","gif");
		this.documentExt = new Vector<String>();
		this.addDocumentExt("docx","pdf","txt");
		this.audioExt = new Vector<String>();
		this.addAudioExt("mp3","ogg");
		this.iframes = new Vector<String>();
	}
	/**
	 * builder with a target url
	 * @param url
	 * @return
	 */
	public static Scraper source(String url)
	{	instance = new Scraper();
		instance.rootURL = url;
		try 
		{
			baseURL = "http://"+url.split("://")[1].split("/")[0];
			htmlpage = Jsoup.connect(url).get();
			if(!htmlpage.select("iframe").isEmpty())
				instance.iframes.addElement(htmlpage.select("iframe").iterator().next().attr("src"));
			return instance;			
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			baseURL = "http://"+url+"/";
			return instance ;
		}
		catch (Exception e) 
		{	System.out.println("Error ====> " + e.getMessage());
		return instance ;
		}
	}
	public static Scraper source(Loader load)
	{
		instance = new Scraper();
		instance.loader = load ;
		htmlpage = Jsoup.parse(load.getHtmlContent()) ;
		return instance ;
	}
	
	/**
	 * save scraping result
	 * @param url
	 * @return
	 */
	public Scraper saveToexcel(ExcelWrite writer)
	{	
		writer.saveScrapingResult(this);
		return this;
	}
	
	/**
	 * save search key result
	 * @param url
	 * @return
	 */
	public Scraper saveToexcel(ExcelWrite writer,String key)
	{	
		writer.saveSearchResult(this.rootURL, key);
		return this;
	}
	
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
        	return emails ;
        
	}
		// getting phone numbers via a regex
	public Set<String> getPhones() 
	{
		this.phones = new HashSet<String>();
		Pattern phonePattern = Pattern.compile("^((((\\(\\d{3}\\))|(\\d{3}-))\\d{3}-\\d{4})|(\\+?\\d{2}((-| )\\d{1,8}){1,5}))(( x| ext)\\d{1,5}){0,1}$");
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
			 String href=link.attr("abs:href");
			
				 if(href.endsWith("#"))
						 href = href.substring(0, href.length()-1);
				 this.Links.add(href);
			 
		 }
		 this.fetchLinks();
	        	return Links;
	}
	/**
	 * @return array of links wanted
	 */
	//getting social media links
	private Set<String> getSocialLinks(String socialmedia) 
	{
		this.SocialLinks = new HashSet<String>();
		for(String link :this.getLinks())
		{
			if (link.contains(socialmedia))
				this.SocialLinks.add(link);
		}
		return SocialLinks;
	}
	
	public Set<String> getSocialMediaLinks()
	{
		Set<String> socialLinks = new HashSet<String>();
		socialLinks.addAll(this.getSocialLinks("facebook"));
		socialLinks.addAll(this.getSocialLinks("twitter"));
		socialLinks.addAll(this.getSocialLinks("fb"));
		socialLinks.addAll(this.getSocialLinks("linkedin"));
		socialLinks.addAll(this.getSocialLinks("instagram"));
		socialLinks.addAll(this.getSocialLinks("pintrest"));
		return socialLinks;
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
					imageMedia.add(meds.attr("abs:src").split("\\?")[0]);
				}
				else if(this.documentExt.contains(ext))
				{
					documentMedia.add(meds.attr("abs:src").split("\\?")[0]);
				}
				else if (this.videoExt.contains(ext))
				{
					videoMedia.add(meds.attr("abs:src").split("\\?")[0]);
				}
				else if (this.audioExt.contains(ext))
				{
					audioMedia.add(meds.attr("abs:src").split("\\?")[0]);
				}
				else
					unknownMedia.add(meds.attr("abs:src").split("\\?")[0]);
	 	   
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
	public void addVideoExt(String... videosExt) 
	{
		for(String ext:videosExt)
			this.videoExt.add(ext);
	}
	private Vector<String> getImagesExt() 
	{
		return imagesExt;
	}
	public void addImageExt(String... imagesExt) 
	{
		for(String ext:imagesExt)
		this.imagesExt.add(ext);
	}
	private Vector<String> getDocumentExt() 
	{
		return documentExt;
	}
	public void addDocumentExt(String... documentsExt) 
	{
		for(String ext:documentsExt)
			this.documentExt.add(ext);
	}
	
	private Vector<String> getAudioExt() {
		return audioExt;
	}
	public void addAudioExt(String... audiosExt) 
	{
		for(String ext:audiosExt)
			this.audioExt.add(ext);
	}
		
	// Fetching internal and external links by the base url and redirect words...
	private void fetchLinks()
	{
		this.externalLinks = new HashSet<String>();
		this.internalLinks = new HashSet<String>();
		for(String link : this.Links)
		{
			if((link.contains(baseURL)||link.startsWith("/"))
					&&!link.contains("URL=")
					&&!link.contains("redirect")
					&&!link.split("://")[1].split(":")[0].contains("http"))
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

	public String getRootURL()
	{
		return this.rootURL;
	}
	
	public Loader getLoader()
	{
		return this.loader;
	}
	
	/**
	 * save scraped to excel file xls or xlsx
	 * @param write
	 * @return
	 */
	public Scraper withExcelSave(ExcelWrite write)
	{
		write.saveScrapingResult(this);
		return this ;
	}
	
	/**
	 * filter files with key tags
	 * @param strings
	 * @return
	 */
	public Scraper withMediaFilter(String...strings)
	{
		this.mediaFilterKeys = strings;
		return this ;
	}
	
	private static int i=0;	
	/**
	 * download medias to deirectory
	 * @param media
	 * @param path
	 * @return
	 */
	public Scraper downloadMedias(String media,String path)
	{
		Download.mkdir(path);
		if(media.equalsIgnoreCase("image"))
			{
			Set<String> images=new HashSet<String>(this.getMedias().get("image"));
			if(this.mediaFilterKeys != null)
				images = Filter.FilterMedia(new HashSet<String>(this.getMedias().get("image")), this.mediaFilterKeys);
			for(String image : images)
				try {
					Download.downloadImage(image, path+"image"+i);
					i++;
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
			}
		return this ;
	}

	private boolean hasFileExtension(String s) 
	{
	    return s.matches("^[\\w\\d\\:\\/\\.]+\\.\\w{3,4}(\\?[\\w\\W]*)?$");
	}
}