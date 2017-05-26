package loaders;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import scrappers.Scraper;

public class HtmlUnitLoader {



	public static String HtmlContent(String url)
	{
		String htmlcontent = "";
		try (final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_52)) {
	        HtmlPage page=null;
			try {
				page = webClient.getPage(url);
				
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
	}
	public static String getPageContent(String url,int pagenumber)
	{
		String htmlcontent = "";
		try (final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_52)) {
	        HtmlPage page=null;
			try {
				page = webClient.getPage(url);
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
		
	}
	
}