package scrappers;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
/**
 * 
 * @author MDA
 *
 */
public class Search {
	
	private static Search instance;
	
	private static Document sourceDoc ;
	private Set<Element> matchedTags = new HashSet<Element>();
	public static Search source(String url)
	{
		try 
		{
			sourceDoc = Jsoup.connect(url).get();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return new Search();
	}
	/**
	 * Check if a keyword existe in a webpage
	 * @param keyword keyword used for the search
	 * @return returns true if the keyword exist 
	 */
	public boolean exist(String keyword)
	{
		if(Pattern.compile(keyword).matcher(sourceDoc.text()).find())
			return true;
		return false;
	}
	/**
	 * Find the tag with matched keyword and return its text
	 * @param keyword
	 * @return Set of text block in which the keywork existe
	 */
	public Set<String> find(String keyword)
	{
		Set<String> results = new HashSet<String>();
		System.out.println(results.toString());
		Iterator<Element> elementIterator = this.deepSearch(sourceDoc.select("html").first(),keyword).iterator();
		while(elementIterator.hasNext())
		{
			results.add(elementIterator.next().text());
			
		}
		return results;
	}
	/**
	 * Search keyword in a specific node
	 * @param node root node to search in
	 * @param keyword search keyword
	 * @return Set of node elements to fetch
	 */
	public Set<Element> deepSearch(Element node,String keyword)
	{
		Pattern  searchKeyPattern= Pattern.compile(keyword,Pattern.CASE_INSENSITIVE);		
		if(searchKeyPattern.matcher(node.ownText()).find())
		{
			matchedTags.add(node);
		}
		if(node.childNodeSize()==0)
		{
			return matchedTags;
		}
		else
		{
			for(int i=0; i<node.children().size();i++)
				deepSearch(node.child(i),keyword);
			return matchedTags;
		}
	}

}
