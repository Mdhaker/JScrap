package utils;

public class Utility {

	public static String formatHTTPS(String http)
	{
		return "https"+http.split("http")[1];
	}
	public void wait(boolean test)
	{
		while(!test)
		{
			
		}
	}
	public static boolean isWindows()
	{
		return (System.getProperty("os.name").equalsIgnoreCase("Windows"));
	}
	
	public static boolean isLinux()
	{
		return (System.getProperty("os.name").equalsIgnoreCase("linux"));
	}
}
