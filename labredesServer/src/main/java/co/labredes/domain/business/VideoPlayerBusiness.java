package co.labredes.domain.business;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import co.labredes.infrastruncture.exceptions.IException;
import fj.data.Either;

public class VideoPlayerBusiness {
	
	private String path = "/home/cis/workspace/labredes6";
	
	private final File folder = new File(path);
	
	public VideoPlayerBusiness(){
		
	}
	
	public Either<IException, List<String>> getVideoList(){
		
		Either<IException, List<String>> either = null;
		
		try{
		
		List<String> fileList = listFilesForFolder(folder);
		
		either = Either.right(fileList);
		
		} catch(Exception e) {
			
		}
		
		return either;

	}
	
	private List<String> listFilesForFolder(final File folder) {
		
		List<String> fileList = new ArrayList<String>();
		
	    for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	        	//Do nothing
	        } else {
	        	
	        	fileList.add(fileEntry.getName());
	        }
	    }
	    
	    return fileList;
	}

	public Either<IOException, String> uploadVideo(InputStream videoFile, String videoName) {
		
		Either<IOException, String> either = null;
		
		byte[] video;
		
		try {
			
			video = org.apache.commons.io.IOUtils.toByteArray(videoFile);
			FileOutputStream fos = new FileOutputStream(path + "/" + videoName);
			fos.write(video);
			fos.close();
			
			String successMessage = "The video " + videoName + " was uploaded successfully";
			
			either = Either.right(successMessage);
			
		} catch (IOException e) {
			
			either = Either.left(e);
			
		}
		
		return either;
	}

	public Either<Exception, String> playVideo() {
		
		Either<Exception, String> either = null;
		
		try{

            ProcessBuilder builder = new ProcessBuilder("/usr/local/bin/python", "src/ServerUDP.py", "239.255.0.1", "9001", "src/video1.mp4");
            builder.redirectErrorStream(true);
            builder.start();
          
            String successMessage = "The video is streaming successfully";
            
            either = Either.right(successMessage);
            
        } catch (Exception e){
        	
            either = Either.left(e);
        }
		
		return either;
	}

}
