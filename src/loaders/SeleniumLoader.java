package loaders;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import org.jboss.netty.handler.timeout.TimeoutException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import config.Config;

public class SeleniumLoader extends Loader{
	
	private WebDriver driver;
	public SeleniumLoader(String url)
	{		
		super(url);
		System.setProperty("webdriver.chrome.verboseLogging", "false");
		System.setProperty("webdriver.chrome.driver", Config.SELENIUM_CHROME_DRIVER_PATH);
         driver = new ChromeDriver();       
        driver.get(url);
        
	}
	public  String getHtmlContent()
	{
		return this.driver.getPageSource();
	}
	public  String getPagedContent(int pagenumber)
	{
				String htmlContent ="";
				List<WebElement> anchors = this.driver.findElements(By.tagName("a"));
				for(WebElement anchor: anchors)
				{
					try
					{
						if(anchor.getText().equalsIgnoreCase("page"+pagenumber)
								||anchor.getText().equalsIgnoreCase("page "+pagenumber)
								||anchor.getText().equalsIgnoreCase(""+pagenumber)&&!anchor.getAttribute("href").contains("rating"))
						{
							//System.out.println("Pagination anchors : "+anchor.getAttribute("href"));
							anchor.click();
							this.selniuemWait(10);
							this.driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
							return this.driver.getPageSource();
						}
						
					}
					catch(org.openqa.selenium.WebDriverException e)
					{
						//System.err.println(e.getMessage());
					}
			}
				
				return htmlContent;
		
	}
	@Override
	public String getContentByClick(String linkText) 
	{
		WebElement elementToClick = this.driver.findElement(By.linkText(linkText));
		elementToClick.click();
		this.selniuemWait(10);
		String result = this.driver.getPageSource();
		this.driver.navigate().back();
		return result ;
	}
	@Override
	public String login(String login, String password, String redirect) 
	{
		this.driver.findElement(By.id("id_username")).sendKeys(login);     
	    this.driver.findElement(By.id("id_password")).sendKeys(password);     
	    this.driver.findElement(By.cssSelector("input[value='login']")).submit();
	    this.selniuemWait(10);
	    this.driver.navigate().to(redirect);
	    this.selniuemWait(10);
		return this.driver.getPageSource();
	}
	
	private void selniuemWait(int milliseconds)
	{
		(new WebDriverWait(this.driver, milliseconds)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) 
            {
                return !d.getPageSource().isEmpty();
            }
        });
	}
	@Override
	public String navigateToUrl(String url,boolean back) {
		String result = "";
		
		try
		{this.driver.navigate().to(url);
		this.selniuemWait(10);
		result = this.driver.getPageSource();
		if(back)
		{
			this.driver.navigate().back();
			this.selniuemWait(10);
		}
		}
		catch(TimeoutException e)
		{
		}
		return result;
	}

}
