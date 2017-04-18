import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Search {
	
	private static Search instance = new Search();
	
	private static Document sourceDoc ;
	public static Search source(String url)
	{
		try 
		{
			sourceDoc = Jsoup.connect(url).get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return instance;
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
	public Set<String> find(String keyword)
	{
		Set<String> results = new HashSet<String>();
		results.add(this.deepSearch(sourceDoc.select("html").first(),keyword).iterator().next().text());
		return results;
	}
	private Set<Element> matchedTags = new HashSet<Element>();
	public Set<Element> deepSearch(Element node,String keyword)
	{
		Pattern  searchKeyPattern= Pattern.compile(keyword);		
		System.out.println("Node name = " + node.tagName()+" || Node children = " + node.children().size());
		if(searchKeyPattern.matcher(node.ownText()).find())
		{
			matchedTags.add(node);
			System.out.println("matched");
		}
		if(node.childNodeSize()==0)
		{
			System.out.println("No children found");
			return matchedTags;
		}
		else
		{
			System.out.println("else the recursive nested call");
			for(int i=0; i<node.children().size();i++)
				deepSearch(node.child(i),keyword);
			return matchedTags;
		}
	}

}
