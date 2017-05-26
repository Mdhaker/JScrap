package utils;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Filter {

	public static Set<String> FilterMedia(Set<String> list, String... keys)
	{
		Set<String> result = new HashSet<String>();
		for(String url : list)
		{
			for(String key:keys)				
			{
				if(url.substring(url.lastIndexOf('/')+1).contains(key))
					result.add(url);
			}				
		}
		return result;
	}
}
