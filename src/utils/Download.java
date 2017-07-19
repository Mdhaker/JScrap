package utils;


import java.net.*;

import org.apache.commons.io.FileUtils;

import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

public class Download {
	
	public static void downloadImage(String image_url, String filename_to_save) throws IOException {
        String extension = image_url.substring(image_url.lastIndexOf("."), image_url.length());
        if(extension != null) {
            filename_to_save += extension;
        }
        InputStream inputStream = new BufferedInputStream(new URL(image_url).openStream());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int n = 0;
        while((n = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, n);
        }
        inputStream.close();
        outputStream.close();
        
        // writing the downloaded image stream to a file
        
        FileOutputStream fileWriter = new FileOutputStream(filename_to_save);
        fileWriter.write(outputStream.toByteArray());
        fileWriter.close();
        System.out.println(filename_to_save);
    }
	
	public static void mkdir(String path)
	{
		File theDir = new File(path);

		// if the directory does not exist, create it
		if (!theDir.exists()) {
		    System.out.println("creating directory: " + theDir.getName());
		    boolean result = false;

		    try{
		        theDir.mkdir();
		        result = true;
		    } 
		    catch(SecurityException se){
		        //handle it
		    }        
		    if(result) {    
		        System.out.println("DIR created");  
		    }
		}
		else
		{
			System.out.println("Directory exist");
		}
	}
	
	// Using Java IO
	 public static void saveFileFromUrlWithJavaIO(String fileName, String fileUrl)
	 throws MalformedURLException, IOException {
	 BufferedInputStream in = null;
	 FileOutputStream fout = null;
	 try {
	 in = new BufferedInputStream(new URL(fileUrl).openStream());
	 fout = new FileOutputStream(fileName);

	byte data[] = new byte[1024];
	 int count;
	 while ((count = in.read(data, 0, 1024)) != -1) {
	 fout.write(data, 0, count);
	 }
	 } finally {
	 if (in != null)
	 in.close();
	 if (fout != null)
	 fout.close();
	 }
	 }

	// Using Commons IO library
	 // Available at http://commons.apache.org/io/download_io.cgi
	 public static void saveFileFromUrlWithCommonsIO(String fileName,
	 String fileUrl) throws MalformedURLException, IOException {
		 //System.out.println("this is the file name : "+fileName+" this the url to donwload "+fileUrl);
	 FileUtils.copyURLToFile(new URL(fileUrl), new File(fileName));
	 }


}
