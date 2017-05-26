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
	
	public static Loader getSeleniumLoader(String url)
	{
		return new SeleniumLoader(url);
	}
	public static Loader getHtmlUnitLoader(String url)
	{
		return new HtmlUnitLoader(url);
	}
}
