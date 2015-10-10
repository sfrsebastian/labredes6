package gp.e3.autheo.authentication.service.resources;

import fj.data.Either;
import gp.e3.autheo.authentication.domain.business.PasswordTokenBusiness;
import gp.e3.autheo.authentication.domain.business.TokenBusiness;
import gp.e3.autheo.authentication.domain.business.UserBusiness;
import gp.e3.autheo.authentication.domain.entities.Citizen;
import gp.e3.autheo.authentication.domain.entities.Token;
import gp.e3.autheo.authentication.domain.entities.User;
import gp.e3.autheo.authentication.infrastructure.exceptions.BusinessException;
import gp.e3.autheo.authentication.infrastructure.exceptions.ExceptionUtils;
import gp.e3.autheo.authentication.infrastructure.exceptions.IException;
import gp.e3.autheo.authentication.infrastructure.exceptions.TechnicalException;
import gp.e3.autheo.authentication.infrastructure.utils.http.HttpCommonResponses;
import gp.e3.autheo.authentication.service.representation.ChangePasswordGivenPasswordToken;
import gp.e3.autheo.authentication.service.representation.CreatePasswordToken;
import gp.e3.autheo.authentication.service.representation.UserImage;
import gp.e3.autheo.authorization.domain.business.RoleBusiness;
import gp.e3.autheo.client.dtos.TokenDTO;
import gp.e3.autheo.infrastructure.exceptions.ExceptionCodes;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.multipart.FormDataParam;


@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    private final UserBusiness userBusiness;
    private final RoleBusiness roleBusiness;
    private final TokenBusiness tokenBusiness;
    private final PasswordTokenBusiness passwordTokenBusiness;

    public UserResource(UserBusiness userBusiness, RoleBusiness roleBusiness, TokenBusiness tokenBusiness, PasswordTokenBusiness passwordTokenBusiness) {

        this.userBusiness = userBusiness;
        this.roleBusiness = roleBusiness;
        this.tokenBusiness = tokenBusiness;
        this.passwordTokenBusiness = passwordTokenBusiness;
    }

    @POST
    public Response createUser(User user) {

        Response response = null;

        if (User.isAValidUser(user)) {

            Either<IException, Boolean> userWasCreatedEither = userBusiness.createUser(user);
            Either<IException, Boolean> tokensWereGeneratedEither = tokenBusiness.generateAndSaveTokensForAnAPIUser(user);
            Either<IException, Boolean> userWasAddedToRoleEither = roleBusiness.addUserToRole(user.getUsername(), user.getBusinessRoleId(), user.getOrganizationId());

            if (userWasCreatedEither.isRight() && tokensWereGeneratedEither.isRight() && userWasAddedToRoleEither.isRight()) {

                response = Response.status(201).build();

            } else {

                List<Either<IException, ?>> eithersList = Arrays.asList(userWasCreatedEither, tokensWereGeneratedEither, userWasAddedToRoleEither);
                Optional<Either<IException, ?>> firstLeftEither = ExceptionUtils.getFirstLeftEither(eithersList);
                IException exception = ExceptionUtils.getIExceptionFromEither(firstLeftEither);
                response = Response.status(500).entity(exception).build();
            }

        } else {

            response = HttpCommonResponses.getInvalidSyntaxResponse();
        }

        return response;
    }

   

    @POST
    @Path("/{username}/tokens")
    public Response authenticateUser(@PathParam("username") String username, User user) {

        Response response = null;

        if ((user != null) && !StringUtils.isBlank(user.getUsername()) && !StringUtils.isBlank(user.getPassword()) && !StringUtils.isBlank(user.getOrganizationId()) ) {

            Either<IException, Boolean> userIsAuthenticatedEither = userBusiness.authenticateUser(user.getUsername(), user.getPassword(), user.getOrganizationId());
            // Get the complete user because its possible that the given user just contains username and password.
            Either<IException, User> userEither = userBusiness.getUserByUsername(user.getUsername(), user.getOrganizationId());

            if (userIsAuthenticatedEither.isRight() && userEither.isRight()) {

                Boolean userIsAuthenticated = userIsAuthenticatedEither.right().value();
                User completeUser = userEither.right().value();
                response = generateUserToken(userIsAuthenticated, completeUser);

            } else {

                Optional<Either<IException, ?>> firstLeftEither = ExceptionUtils.getFirstLeftEither(Arrays.asList(userIsAuthenticatedEither, userEither));
                IException exception = ExceptionUtils.getIExceptionFromEither(firstLeftEither);

                int statusCode = exception instanceof BusinessException ? Status.NOT_FOUND.getStatusCode() : Status.INTERNAL_SERVER_ERROR.getStatusCode();
                response = Response.status(statusCode).entity(exception).build();
            }

        } else {

            response = HttpCommonResponses.getInvalidSyntaxResponse();
        }

        return response;
    }

    @GET
    @Path("/{username}/{organizationId}")
    public Response getUserByUsername(@PathParam("username") String username, @PathParam("organizationId") String organizationId) {

        Response response = null;

        if (!StringUtils.isBlank(username)) {

            Either<IException, User> userEither = userBusiness.getUserByUsername(username, organizationId);

            if (userEither.isRight()) {

                User user = userEither.right().value();
                response = Response.status(200).entity(user).build();

            } else {

                IException exception = userEither.left().value();
                int statusCode = exception instanceof BusinessException ? Status.NOT_FOUND.getStatusCode() : Status.INTERNAL_SERVER_ERROR.getStatusCode();
                response = Response.status(statusCode).entity(exception).build();
            }

        } else {

            response = HttpCommonResponses.getInvalidSyntaxResponse();
        }

        return response;
    }

    @GET
    @Path("/{username}/{tokenValue}/image")
    public Response getUserImageByTokenValue(@PathParam("tokenValue") String tokenValue) {

        Response response = null;

        if (!StringUtils.isBlank(tokenValue)) {

            Either<IException, Token> apiTokenEither = tokenBusiness.getAPIToken(tokenValue);

            if (apiTokenEither.isRight()) {

                Token apiToken = apiTokenEither.right().value();
                Either<IException, User> userEither = userBusiness.getUserByUsername(apiToken.getUsername(), apiToken.getUserOrganization());

                if(userEither.isRight()) {

                    User user = userEither.right().value();
                    UserImage userImage = new UserImage(user.getImage());

                    response = Response.status(Response.Status.OK).entity(userImage).build();

                } else {

                    IException exception = apiTokenEither.left().value();
                    int statusCode = exception instanceof BusinessException ? Status.NOT_FOUND.getStatusCode() : Status.INTERNAL_SERVER_ERROR.getStatusCode();
                    response = Response.status(statusCode).entity(exception).build();
                }

            } else {

                IException exception = apiTokenEither.left().value();
                int statusCode = exception instanceof BusinessException ? Status.NOT_FOUND.getStatusCode() : Status.INTERNAL_SERVER_ERROR.getStatusCode();
                response = Response.status(statusCode).entity(exception).build();
            }

        } else {

            response = HttpCommonResponses.getInvalidSyntaxResponse();
        }

        return response;
    }

    @GET
    public Response getUsersByRoleId(@QueryParam("roleId") String roleId, @QueryParam("tenantId") String tenantId) {

        Response response = null;

        if (!StringUtils.isBlank(roleId)) {

            Either<IException, List<User>> usersListEither = userBusiness.getUsersByRoleId(roleId, tenantId);

            if (usersListEither.isRight()) {

                List<User> usersList = usersListEither.right().value();
                response = Response.status(200).entity(usersList).build();

            } else {

                IException exception = usersListEither.left().value();
                response = Response.status(Status.OK).entity(exception).build();
            }

        } else {

            response = HttpCommonResponses.getInvalidSyntaxResponse();
        }

        return response;
    }


    @PUT
    @Path("/{username}")
    public Response updateUser(@PathParam("username") String username, User updatedUser) {

        boolean updatedUserIsValid = (updatedUser != null) && !StringUtils.isBlank(updatedUser.getName()) && 
                !StringUtils.isBlank(updatedUser.getPassword());

        Response response = null;

        if (!StringUtils.isBlank(username) && updatedUserIsValid) {

            Either<IException, Boolean> userWasUpdatedEither = userBusiness.updateUser(username, updatedUser);

            if (userWasUpdatedEither.isRight()) {

                Boolean userWasUpdated = userWasUpdatedEither.right().value();
                Status statusCode = userWasUpdated ? Status.OK : Status.INTERNAL_SERVER_ERROR;
                response = Response.status(statusCode).build();

            } else {

                response = Response.status(500).build();
            }

        } else {

            response = HttpCommonResponses.getInvalidSyntaxResponse();
        }

        return response;
    }

    @DELETE
    @Path("/{username}")
    public Response deleteUser(@PathParam("username") String username) {

        Response response = null;

        if (!StringUtils.isBlank(username)) {

            Either<IException, Boolean> userWasDeletedEither = userBusiness.deleteUser(username);

            if (userWasDeletedEither.isRight()) {

                Boolean userWasDeleted = userWasDeletedEither.right().value();
                Status statusCode = userWasDeleted ? Status.OK : Status.INTERNAL_SERVER_ERROR;
                response = Response.status(statusCode).build();

            } else {

                response = Response.status(500).build();
            }

        } else {

            response = HttpCommonResponses.getInvalidSyntaxResponse();
        }

        return response;
    }

    @GET
    @Path("/documents/{documentId}")
    public Response userDocumentExists(@Context TokenDTO token, @PathParam("documentId") String documentId) {

        Response response = null;

        if (!StringUtils.isBlank(documentId)) {

            Either<IException, Boolean> userDocumentExistsEither = userBusiness.userDocumentExists(documentId, token.getUserOrganization());

            if (userDocumentExistsEither.isRight()) {

                Boolean userDocumentExists = userDocumentExistsEither.right().value();
                Status statusCode = Status.OK;
                Map<String, Boolean> responseMap = new HashMap<String, Boolean>();
                responseMap.put("documentExists", userDocumentExists);
                response = Response.status(statusCode).entity(responseMap).build();

            } else {

                response = Response.status(500).build();
            }

        } else {

            response = HttpCommonResponses.getInvalidSyntaxResponse();
        }

        return response;
    }
    
    @GET
    @Path("/citizens/documents/{documentId}")
    public Response userDocumentExists( @PathParam("documentId") String documentId, @QueryParam ("organizationId") String organizationId) {

        Response response = null;

        if (!StringUtils.isBlank(documentId) && !StringUtils.isBlank(organizationId)) {

            Either<IException, Boolean> userDocumentExistsEither = userBusiness.userDocumentExists(documentId, organizationId);

            if (userDocumentExistsEither.isRight()) {

                Boolean userDocumentExists = userDocumentExistsEither.right().value();
                Status statusCode = Status.OK;
                Map<String, Boolean> responseMap = new HashMap<String, Boolean>();
                responseMap.put("documentExists", userDocumentExists);
                response = Response.status(statusCode).entity(responseMap).build();

            } else {

                response = Response.status(500).build();
            }

        } else {

            response = HttpCommonResponses.getInvalidSyntaxResponse();
        }

        return response;
    }
    
    @GET
    @Path("/usernames/{username}")
    public Response usernameExists(@Context TokenDTO token, @PathParam("username") String username) {

        Response response = null;

        if (!StringUtils.isBlank(username)) {

            Either<IException, Boolean> userExistsEither = userBusiness.usernameExists(username, token.getUserOrganization());

            if (userExistsEither.isRight()) {

                Boolean userExists = userExistsEither.right().value();
                Status statusCode = Status.OK;
                Map<String, Boolean> responseMap = new HashMap<String, Boolean>();
                responseMap.put("usernameExists", userExists);
                response = Response.status(statusCode).entity(responseMap).build();

            } else {

                response = Response.status(500).build();
            }

        } else {

            response = HttpCommonResponses.getInvalidSyntaxResponse();
        }

        return response;
    }
    
    @GET
    @Path("/citizens/usernames/{username}")
    public Response validateUserNameExists( @PathParam("username") String username, @QueryParam ("organizationId") String organizationId) {

        Response response = null;

        if (!StringUtils.isBlank(username) && !StringUtils.isBlank(organizationId)) {

            Either<IException, Boolean> userExistsEither = userBusiness.usernameExists(username, organizationId);

            if (userExistsEither.isRight()) {

                Boolean userExists = userExistsEither.right().value();
                Status statusCode = Status.OK;
                Map<String, Boolean> responseMap = new HashMap<String, Boolean>();
                responseMap.put("usernameExists", userExists);	
                response = Response.status(statusCode).entity(responseMap).build();

            } else {

                response = Response.status(500).build();
            }

        } else {

            response = HttpCommonResponses.getInvalidSyntaxResponse();
        }

        return response;
    }

    @POST
    @Path("/photo")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUserWithPhoto(@FormDataParam("file") InputStream uploadedInputStream, @FormDataParam("user") String jsonUser) {

        Response response = null;

        try {

            ObjectMapper objectMapper = new ObjectMapper();
            User user  = objectMapper.readValue(jsonUser, User.class);
            byte[] photo = org.apache.commons.io.IOUtils.toByteArray(uploadedInputStream);
            
            if (User.isAValidUser(user)) {

                Either<IException, Boolean> userWasCreatedEither = userBusiness.createUserWithPhoto(user, photo);
                if (userWasCreatedEither.isRight()) {

                    response = Response.status(201).build();

                } else {

                    IException exception = ExceptionUtils.getIExceptionFromEither(userWasCreatedEither);
                    response = Response.status(500).entity(exception).build();
                }

            } else {

                response = HttpCommonResponses.getInvalidSyntaxResponse();
            }          
            
        } catch (Exception e) {
            
            IException exception = new TechnicalException(e.getMessage());
            response = Response.status(500).entity(exception).build();
            
        } finally {
        	
        	try {
        		
				uploadedInputStream.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        }
        return response;
    } 
    
    @POST
    @Path("/password-tokens")
    public Response generatePasswordToken(CreatePasswordToken createPasswordToken) {

        Response response = null;

        if (!StringUtils.isBlank(createPasswordToken.getEmail()) && !StringUtils.isBlank(createPasswordToken.getOrganizationId())) {

            Either<IException, Boolean> generatedPasswordTokenEither = passwordTokenBusiness.generatePasswordToken(createPasswordToken.getEmail(), createPasswordToken.getOrganizationId(), 
                                                                        createPasswordToken.isCitizen());

            if (generatedPasswordTokenEither.isRight()) {

                response = Response.status(Status.OK).build();

            } else {

                IException exception = generatedPasswordTokenEither.left().value();
                int status = ExceptionCodes.getStatusCode(exception);
                response = Response.status(status).entity(exception).build();
            }

        } else {

            response = HttpCommonResponses.getInvalidSyntaxResponse();
        }

        return response;
    }
    
    @PUT
    @Path("/password-tokens")
    public Response changePasswordGivenPasswordToken(ChangePasswordGivenPasswordToken changePasswordGivenPasswordToken) {

        Response response = null;

        if (!StringUtils.isBlank(changePasswordGivenPasswordToken.getPasswordToken()) && !StringUtils.isBlank(changePasswordGivenPasswordToken.getHashedNewPassword())) {

            Either<IException, Boolean> passwordWasChangedEither = 
                            passwordTokenBusiness.changePasswordGivenPasswordToken(changePasswordGivenPasswordToken.getPasswordToken(), changePasswordGivenPasswordToken.getHashedNewPassword(), 
                            changePasswordGivenPasswordToken.getOrganizationId());
            
            if (passwordWasChangedEither.isRight()) {

                response = Response.status(Status.OK).build();

            } else {

                IException exception = passwordWasChangedEither.left().value();
                int status = ExceptionCodes.getStatusCode(exception);
                response = Response.status(status).entity(exception).build();
            }

        } else {

            response = HttpCommonResponses.getInvalidSyntaxResponse();
        }

        return response;
    }
    
    @POST    
    @Path("/citizens")
    public Response createCitizen(Citizen citizen) {

        Response response = null;

        if (citizen != null) {

            Either <IException, Boolean> userWasCreatedEither = userBusiness.createCitizen(citizen);
            
            if (userWasCreatedEither.isRight()) {

                response = Response.status(201).build();

            } else {

                IException exception = ExceptionUtils.getIExceptionFromEither(userWasCreatedEither);
                response = Response.status(500).entity(exception).build();
            }

        } else {

            response = HttpCommonResponses.getInvalidSyntaxResponse();
        }

        return response;
    }

    ////////////////////////
    // Private Methods
    ////////////////////////
    
    
    private Response generateUserToken(Boolean userIsAuthenticated, User completeUser) {

        Response response = null;

        if (userIsAuthenticated) {

            Either<IException, Token> tokenEither = tokenBusiness.generateToken(completeUser);

            if (tokenEither.isRight()) {

                Token token = tokenEither.right().value();
                response = Response.status(Status.CREATED).entity(token).build();

            } else {

                IException exception = tokenEither.left().value();
                response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(exception).build();
            }

        } else {
            IException exception = new BusinessException("Usuario o contraseña incorrectos");
            response = Response.status(Status.UNAUTHORIZED).entity(exception).build();
        }

        return response;
    }
    
   
    
}
