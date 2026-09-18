package com.kelvin.nationallibraryproject.utils;

import java.io.*;
import java.nio.file.*;
 
import org.springframework.web.multipart.MultipartFile;

public class FileUploadUtil {
	
	
	 // this utility class is only responsible for creating the directory if not exists, 
	// and save the uploaded file from MultipartFile object to a file in the file system.
	
	     
	    public static void saveFile(String uploadDir, String fileName,
	            MultipartFile multipartFile) throws IOException {
	        Path uploadPath = Paths.get(uploadDir);
	         
	        if (!Files.exists(uploadPath)) {
	            Files.createDirectories(uploadPath);
	        }
	         
	        try (InputStream inputStream = multipartFile.getInputStream()) {
	            Path filePath = uploadPath.resolve(fileName);
	            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
	        } catch (IOException ioe) {        
	            throw new IOException("Could not save image file: " + fileName, ioe);
	        }      
	    }

}
