package gp.e3.autheo.authentication.service.resources;

import java.io.IOException;
import java.io.InputStream;

import fj.data.Either;
import gp.e3.autheo.authentication.domain.business.OrganizationBusiness;
import gp.e3.autheo.authentication.domain.business.TokenBusiness;
import gp.e3.autheo.authentication.domain.business.UserBusiness;
import gp.e3.autheo.authentication.domain.entities.TenantInfo;
import gp.e3.autheo.authentication.domain.entities.Token;
import gp.e3.autheo.authentication.domain.entities.User;
import gp.e3.autheo.authentication.infrastructure.exceptions.IException;
import gp.e3.autheo.authentication.infrastructure.utils.http.HttpCommonResponses;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.multipart.FormDataParam;

@Path("/organizations")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OrganizationResource {

    private final OrganizationBusiness organizationBusiness;
    
    /**
     * An object used to log.
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(UserBusiness.class);

    public OrganizationResource(TokenBusiness tokenBusiness, OrganizationBusiness organizationBusiness) {

        //this.tokenBusiness = tokenBusiness;
        this.organizationBusiness = organizationBusiness;
    }

    //TODO revisar que hace esto!
//    @GET
//    @Path("/{organizationId}/module-token")
//    public Response getModuleTokenByUserOrganization(@PathParam("organizationId") String organizationId) {
//
//        Response response = null;
//
//        if (!StringUtils.isBlank(organizationId)) {
//
//            Either<IException, Token> moduleTokenEither = tokenBusiness.getModuleToken(organizationId);
//
//            if (moduleTokenEither.isRight()) {
//
//                Token moduleToken = moduleTokenEither.right().value();
//                response = Response.status(200).entity(moduleToken).build();
//
//            } else {
//
//                IException exception = moduleTokenEither.left().value();
//                response = Response.status(404).entity(exception).build();
//            }
//
//        } else {
//
//            response = HttpCommonResponses.getInvalidSyntaxResponse();
//        }
//
//        return response;
//    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createOrganization(@FormDataParam("file") InputStream uploadedInputStream, @FormDataParam("tenantInfo") String jsonUser) {

        Response response = null;

        try {
            if (!StringUtils.isBlank(jsonUser) && uploadedInputStream != null) {

                ObjectMapper objectMapper = new ObjectMapper();
                TenantInfo tenantInfo = objectMapper.readValue(jsonUser, TenantInfo.class);
                byte[] photo = org.apache.commons.io.IOUtils.toByteArray(uploadedInputStream);

                System.out.println("photo: "+photo.length);
                
                Either<IException, Boolean> moduleTokenEither = organizationBusiness.createTenant(tenantInfo, photo);

                if (moduleTokenEither.isRight()) {

                    boolean moduleToken = moduleTokenEither.right().value();
                    response = Response.status(200).entity(moduleToken).build();

                } else {

                    IException exception = moduleTokenEither.left().value();
                    response = Response.status(500).entity(exception).build();
                    LOGGER.error("OrganizationResource :: createOrganization", exception);
                }

            } else {

                response = HttpCommonResponses.getInvalidSyntaxResponse();
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return response;
    }
}