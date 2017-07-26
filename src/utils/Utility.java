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
}
