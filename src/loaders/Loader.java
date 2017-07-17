package loaders;

public abstract class  Loader {

	private String url ;
	public Loader(String url)
	{
		this.url = url;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * Load html content of the page
	 * @return html string of the page
	 */
	public abstract String getHtmlContent();
	/**
	 * get ajax page content
	 * @param page page number to fetch and load
	 * @return html string of the page
	 */
	public abstract String getPagedContent(int page);
	
	/**
	 * Click a html element fetched by a css selector to load new content
	 * @param selector css selector of the element
	 * @return
	 */
	public abstract String getContentByClick(String selector);
	/**
	 * Naviagte to url in the same browser session and loaded it content
	 * @param url url to navigate
	 * @param back back to the main page or no
	 * @return
	 */
	public abstract String navigateToUrl(String url,boolean back);
	
	/**
	 * login to the provided site automaticallu
	 * @param login login to send
	 * @param password password to send
	 * @param redirect page to get back to
	 * @return 
	 */
	
	public abstract String login(String login,String password,String redirect);
	
	public static Loader getSeleniumLoader(String url)
	{
		return new SeleniumLoader(url);
	}
	public static Loader getHtmlUnitLoader(String url)
	{
		return new HtmlUnitLoader(url);
	}
}
