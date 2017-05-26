package loaders;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import config.Config;

public class SeleniumLoader extends Loader{
	
	private WebDriver driver;
	public SeleniumLoader(String url)
	{
		super(url);
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
				List<WebElement> anchors = this.driver.findElements(By.name("a"));
				for(WebElement anchor: anchors)
				{
					if(anchor.getAttribute("href").equals("#")||anchor.getAttribute("href").equals("#"))
					{
						System.out.println(anchor.getText());
						if(anchor.getText().contains("page"+pagenumber)
								||anchor.getText().contains("page "+pagenumber)
								||anchor.getText().equals(""+pagenumber))
						{
							System.out.println(anchor.getText());
							anchor.click();
							(new WebDriverWait(this.driver, 10)).until(new ExpectedCondition<Boolean>() {
					            public Boolean apply(WebDriver d) {
					                return !d.getPageSource().isEmpty();
					            }
					        });
							return this.driver.getPageSource();
						}
						
					}
				}
				return htmlContent;
		
	}
	

}
