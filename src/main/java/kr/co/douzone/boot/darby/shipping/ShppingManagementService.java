package kr.co.douzone.boot.darby.shipping;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;


@Service
public class ShppingManagementService {
	
	/*
	 * ImageURL 을
	 * ByteArray로 변환
	 */
	public String getByteArrayFromImageURL(String imageUrl) {

//		String imageUrl = imgUrl.replace("jpg", "png");
		String imageDataString = "";
	    try {           
	        // Reading a Image file from file system
	        URL url = new URL(imageUrl); 
	        InputStream is = url.openStream(); 
	        byte[] bytes = IOUtils.toByteArray(is); 

//	        FileInputStream imageInFile = new FileInputStream(is.toString());
//	        byte imageData[] = new byte[2048];
//	        imageInFile.read(imageData);

	        // Converting Image byte array into Base64 String
	        imageDataString = encodeImage(bytes); 
	        System.out.println("imageDataString : " + imageDataString);
	        
	        
	        System.out.println("Image Successfully Manipulated!");
	    } catch (FileNotFoundException e) {
	        System.out.println("Image not found" + e);
	    } catch (IOException ioe) {
	        System.out.println("Exception while reading the Image " + ioe);
	    }
	    
	    return imageDataString;
	}
	
	public static String encodeImage(byte[] imageByteArray) {
	    return Base64.encodeBase64URLSafeString(imageByteArray);
	}
}
