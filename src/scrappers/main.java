package scrappers;

import loaders.HtmlUnitLoader;
import loaders.Loader;
import utils.ExcelWrite;
import utils.Rinterface;

public class main {

	public static void main(String[] args) 
	{ 
		Rinterface r = new Rinterface("http://www.poste.tn/contact.php?code_menu=8");
		r.setChromeDriverPath("/home/dhaker/datacollector/");
		String path ="/home/dhaker/Desktop/ThisDirectoryForTest/";
		r.scrapInFile(path+"/scrapfilepost.xls");
	}
}
