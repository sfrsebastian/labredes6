package co.labredes.domain.business;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import co.labredes.infrastruncture.exceptions.IException;
import fj.data.Either;

public class VideoPlayerBusiness {
	
	final File folder = new File("/home/you/Desktop");
	
	public VideoPlayerBusiness(){
		
	}
	
	public Either<IException, List<String>> getMovieList(){
		try{
			
		Either<IException, List<String>> either = null;
		
		File file = new File("/home/cis/workspace/labredes6");
		
		List<String> fileList = listFilesForFolder(file);
		
		either = Either.right(fileList);
		
		} catch(Exception e) {
			
		}
		
		return fileList;

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

}
