package gp.e3.autheo.authorization.service.resources;

import fj.data.Either;
import gp.e3.autheo.authentication.domain.entities.User;
import gp.e3.autheo.authentication.infrastructure.exceptions.IException;
import gp.e3.autheo.authentication.infrastructure.utils.http.HttpCommonResponses;
import gp.e3.autheo.authorization.domain.business.RoleBusiness;
import gp.e3.autheo.authorization.domain.entities.BusinessRole;
import gp.e3.autheo.authorization.domain.entities.Permission;
import gp.e3.autheo.authorization.domain.entities.Role;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.StringUtils;

@Path("/roles")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RoleResource {

    private final RoleBusiness roleBusiness;

    public RoleResource(RoleBusiness roleBusiness) {

        this.roleBusiness = roleBusiness;
    }

    @POST
    public Response createRole(Role role) {

        Response response = null;

        if (Role.isValidRole(role)) {

            Either<IException, Role> createdRoleEither = roleBusiness.createRole(role);

            if (createdRoleEither.isRight()) {

                Role createdRole = createdRoleEither.right().value();
                response = Response.status(Status.CREATED).entity(createdRole).build();

            } else {

                IException exception = createdRoleEither.left().value();
                response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(exception).build();
            }

        } else {

            response = HttpCommonResponses.getInvalidSyntaxResponse();
        }

        return response;
    }

    @GET
    @Path("/{roleName}")
    public Response getRoleByName(@PathParam("roleName") String roleName) {

        Response response = null;

        if (!StringUtils.isBlank(roleName)) {

            Either<IException, Role> roleEither = roleBusiness.getRoleByName(roleName);

            if (roleEither.isRight()) {

                Role role = roleEither.right().value();
                response = Response.status(Status.OK).entity(role).build();

            } else {

                IException exception = roleEither.left().value();
                response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(exception).build();
            }

        } else {

            response = HttpCommonResponses.getInvalidSyntaxResponse();
        }

        return response;
    }
    
    @GET
    @Path("/citizens/business-roles")
    public Response getCitizenBusinessRoles(@QueryParam("tenantId") String tenatId) {

        Response response = null;

        if (!StringUtils.isBlank(tenatId)) {

            Either<IException, List<String>> roleEither = roleBusiness.getCitizenBusinessRoles(tenatId);

            if (roleEither.isRight()) {
            	List<String> allRolesNames = roleEither.right().value();
                response = Response.status(Status.OK).entity(allRolesNames).build();

            } else {

                IException exception = roleEither.left().value();
                response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(exception).build();
            }

        } else {

            response = HttpCommonResponses.getInvalidSyntaxResponse();
        }

        return response;
    }

    @GET
    public Response getAllRolesNames() {

        Response response = null;
        Either<IException, List<String>> allRolesNamesEither = roleBusiness.getAllRolesNames();

        if (allRolesNamesEither.isRight()) {

            List<String> allRolesNames = allRolesNamesEither.right().value();
            response = Response.status(Status.OK).entity(allRolesNames).build();

        } else {

            IException exception = allRolesNamesEither.left().value();
            response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(exception).build();
        }

        return response;
    }

    @PUT
    @Path("/{roleName}")
    public Response updateRole(@PathParam("roleName") String roleName, Role updatedRole) {

        Response response = null;

        if (!StringUtils.isBlank(roleName) && Role.isValidRole(updatedRole)) {

            roleBusiness.updateRole(roleName, updatedRole.getPermissions());
            response = Response.status(Status.OK).build();

        } else {

            response = HttpCommonResponses.getInvalidSyntaxResponse();
        }

        return response;
    }

    @DELETE
    @Path("/{roleName}")
    public Response deleteRole(@PathParam("roleName") String roleName) {

        Response response = null;

        if (!StringUtils.isBlank(roleName)) {

            roleBusiness.deleteRole(roleName);
            response = Response.status(Status.OK).build();

        } else {

            response = HttpCommonResponses.getInvalidSyntaxResponse();
        }

        return response;
    }

    @POST
    @Path("/{roleName}/users")
    public Response addUserToRole(@PathParam("roleName") String roleName, User user) {

        Response response = null;

        if (!StringUtils.isBlank(roleName) && user != null) {

            String username = user.getUsername();
            if (!StringUtils.isBlank(username)) {

                roleBusiness.addUserToRole(username, roleName, user.getOrganizationId());
                response = Response.status(Status.CREATED).build();

            } else {

                response = HttpCommonResponses.getInvalidSyntaxResponse();
            }

        } else {

            response = HttpCommonResponses.getInvalidSyntaxResponse();
        }

        return response;
    }

    @DELETE
    @Path("/{roleName}/users/{username}")
    public Response removeUserFromRole(@PathParam("roleName") String roleName, @PathParam("username") String username) {

        Response response = null;

        if (!StringUtils.isBlank(roleName) && !StringUtils.isBlank(username)) {

            roleBusiness.removeUserFromRole(username, roleName);
            response = Response.status(Status.OK).build();

        } else {

            response = HttpCommonResponses.getInvalidSyntaxResponse();
        }

        return response;
    }

    @GET
    @Path("/{roleName}/permissions")
    public Response getAllPermissionsOfAGivenRole(@PathParam("roleName") String roleName) {

        Response response = null;

        if (!StringUtils.isBlank(roleName)) {

            Either<IException, List<Permission>> rolePermissionsEither = roleBusiness.getAllPermissionsOfAGivenRole(roleName);

            if (rolePermissionsEither.isRight()) {

                List<Permission> rolePermissions = rolePermissionsEither.right().value();
                response = Response.status(Status.OK).entity(rolePermissions).build();

            } else {

                IException exception = rolePermissionsEither.left().value();
                response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(exception).build();
            }

        } else {

            response = HttpCommonResponses.getInvalidSyntaxResponse();
        }

        return response;
    }

    @GET
    @Path("organizations/{organizationId}")
    public Response getBusinessRoles(@PathParam("organizationId") String organizationId) {

        Response response = null;

        if (!StringUtils.isBlank(organizationId)) {

            Either<IException, List<BusinessRole>> rolesEither = roleBusiness.getBusinessRoles(organizationId);

            if (rolesEither.isRight()) {

                List<BusinessRole> businessRoles = rolesEither.right().value();
                response = Response.status(Status.OK).entity(businessRoles).build();

            } else {

                IException exception = rolesEither.left().value();
                response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(exception).build();
            }

        } else {

            response = HttpCommonResponses.getInvalidSyntaxResponse();
        }

        return response;
    }

    @POST
    @Path("/businessRole")
    public Response createBusinessRole(BusinessRole businessRole){
        Response response = null;
        if( !StringUtils.isBlank(businessRole.getDescription()) 
                && !StringUtils.isBlank(businessRole.getBusinessRole()) && !StringUtils.isBlank(businessRole.getSystemRole())){

            Either<IException, BusinessRole> businessRoleEither = roleBusiness.createBusinessRole(businessRole);
            if (businessRoleEither.isRight()) {

                BusinessRole businessRoleResponse = businessRoleEither.right().value();
                response = Response.status(Status.OK).entity(businessRoleResponse).build();

            } else {

                IException exception = businessRoleEither.left().value();
                response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(exception).build();
            }

        }else{

            response = HttpCommonResponses.getInvalidSyntaxResponse();
        }

        return response;
    }
}