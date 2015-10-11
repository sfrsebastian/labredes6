package co.labredes.api.resources;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sun.jersey.multipart.FormDataParam;

import co.labredes.domain.business.VideoPlayerBusiness;
import co.labredes.infrastruncture.exceptions.IException;
import fj.data.Either;

/**
 * Service that exposes Video player as resources
 * @author Cis
 *
 */
@Path("/video-player")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class VideoPayerResource {
	
	private final VideoPlayerBusiness videoPlayerBusiness;
	
	public VideoPayerResource(VideoPlayerBusiness videoPlayerBusiness){
		this.videoPlayerBusiness = videoPlayerBusiness;
	}
	
    @GET
    public Response getVideoList() {

        Response response = null;

        Either<IException, List<String>> eitherResult = videoPlayerBusiness.getVideoList();

        if (eitherResult.isLeft()) {

            IException exception = eitherResult.left().value();
            response = Response.status(500).entity(exception.getErrorMessage()).build();

        } else {

            List<String> videoList = eitherResult.right().value();
            response = Response.status(200).entity(videoList).build();
        }

        return response;
    }
    
    @POST
    @Path("/upload/{videoName}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadVideo(@FormDataParam("videoFile") InputStream videoFile, @PathParam("videoName") String videoName){

    	Response response = null;
    	
    	Either<IOException, String> either = videoPlayerBusiness.uploadVideo(videoFile,videoName);
    	
    	if (either.isRight()){
    		
    		response = Response.status(200).entity(either.right().value()).build();
    		
    	} else {
    		
    		response = Response.status(500).entity(either.left().value().getMessage()).build();
    		
    	}
    	
    	return response;
    }

}
