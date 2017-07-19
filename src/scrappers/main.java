package scrappers;

import org.apache.commons.exec.OS;

import config.Config;
import loaders.HtmlUnitLoader;
import loaders.Loader;
import utils.ExcelWrite;
import utils.Rinterface;

public class main {

	public static void main(String[] args) 
	{ 
		Rinterface.setChromeDriverPath("/home/dhaker/datacollector/inst");
		Rinterface r = new Rinterface("https://www.planwallpaper.com/wallpapers");
		
		String path ="/home/dhaker/Desktop/ThisDirectoryForTest/";
		//r.scrapInFile(path+"/scrapfilepost.xls");
		//System.out.println(Config.isWindows());
		r.downloadImages(path);
		
	}
}
