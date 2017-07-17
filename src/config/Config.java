package config;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;


public class Config {

	private static PrintStream showStream = System.out;
	private static PrintStream hideStream    = new PrintStream(new Config.HideStream());
	public static boolean Debug = false ;
	public static void showDebug()
	{
		System.setOut(showStream);			
	}
	public static void hideDebug()
	{
		System.setOut(hideStream);
	}
	
	public static String SELENIUM_CHROME_DRIVER_PATH ="/lib/webDriver/chromedriver";
	
	public Config(String driverpath)
	{
		SELENIUM_CHROME_DRIVER_PATH = driverpath;
	}
	private static class HideStream extends OutputStream{

		@Override
		public void write(int b) throws IOException {
			// TODO Auto-generated method stub
			
		}

	}

	
}
