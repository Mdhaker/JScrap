package loaders;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.logging.Level;

import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import scrappers.Scraper;

public class HtmlUnitLoader extends Loader{


	private WebClient webclient ;
	private String url;
	
	public HtmlUnitLoader(String url)
	{
		super(url);
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log","org.apache.commons.logging.impl.NoOpLog");
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
		java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF); 
		this.webclient = new WebClient(BrowserVersion.FIREFOX_52);
		this.url = url ;
	}

	public  String getHtmlContent()
	{
		String htmlcontent = "";
		
	        HtmlPage page=null;
			try {
				page = this.webclient.getPage(url);
				
			} catch (FailingHttpStatusCodeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(page.isHtmlPage())
				return page.asXml();
	        return htmlcontent;
	    
	}
	public String getPagedContent(int pagenumber)
	{
		String htmlcontent = "";
		
	        HtmlPage page=null;
			try {
				page = this.webclient.getPage(url);
				List<HtmlAnchor> anchors = page.getAnchors();
				for(HtmlAnchor anchor: anchors)
				{
					if(anchor.getHrefAttribute().equals("#")||anchor.getHrefAttribute().equals("#"))
					{
						System.out.println(anchor.asText());
						if(anchor.getTextContent().contains("page"+pagenumber)
								||anchor.getTextContent().contains("page "+pagenumber)
								||anchor.getTextContent().equals(""+pagenumber))
						{
							System.out.println(anchor.asText());
							anchor.click();
						}
					}
				}
				
			} catch (FailingHttpStatusCodeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(page.isHtmlPage())
				return page.asXml();
	        return htmlcontent;
	   
		
	}

	@Override
	public String getContentByClick(String selector) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String login(String login, String password, String redirect) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String navigateToUrl(String url,boolean back) {
		// TODO Auto-generated method stub
		return null;
	}

	
}