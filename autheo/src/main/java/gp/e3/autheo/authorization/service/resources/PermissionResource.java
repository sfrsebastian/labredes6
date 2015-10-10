package gp.e3.autheo.authorization.service.resources;

import fj.data.Either;
import gp.e3.autheo.authentication.infrastructure.exceptions.BusinessException;
import gp.e3.autheo.authentication.infrastructure.exceptions.IException;
import gp.e3.autheo.authentication.infrastructure.utils.http.HttpCommonResponses;
import gp.e3.autheo.authorization.domain.business.PermissionBusiness;
import gp.e3.autheo.authorization.domain.entities.Permission;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("/permissions")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PermissionResource {

    private final PermissionBusiness permissionBusiness;

    public PermissionResource(PermissionBusiness permissionBusiness) {

        this.permissionBusiness = permissionBusiness;
    }

    @POST
    public Response createPermission(Permission permission) {

        Response response = null;

        if (Permission.isValidPermission(permission)) {

            Either<IException, Long> permissionIdEither = permissionBusiness.createPermission(permission);

            if (permissionIdEither.isRight()) {

                long permissionId = permissionIdEither.right().value();
                response = Response.status(Status.CREATED).entity(permissionId).build();

            } else {

                IException exception = permissionIdEither.left().value();
                response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(exception).build();
            }

        } else {

            response = HttpCommonResponses.getInvalidSyntaxResponse();
        }

        return response;
    }

    @GET
    @Path("/{permissionId}")
    public Response getPermissionById(@PathParam("permissionId") int permissionId) {

        Response response = null;
        Either<IException, Permission> permissionEiher = permissionBusiness.getPermissionById(permissionId);

        if (permissionEiher.isRight()) {

            Permission permission = permissionEiher.right().value();
            response = Response.status(Status.OK).entity(permission).build();

        } else {

            IException exception = permissionEiher.left().value();
            Status statusCode = exception instanceof BusinessException ? Status.NOT_FOUND : Status.INTERNAL_SERVER_ERROR;
            response = Response.status(statusCode).entity(exception).build();
        }

        return response;
    }

    @GET
    public Response getAllPermissions() {

        Response response = null;
        Either<IException, List<Permission>> allPermissionsEither = permissionBusiness.getAllPermissions();

        if (allPermissionsEither.isRight()) {

            List<Permission> permissionsList = allPermissionsEither.right().value();
            response = Response.status(Status.OK).entity(permissionsList).build();

        } else {

            IException exception = allPermissionsEither.left().value();
            response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(exception).build();
        }

        return response;
    }

    @DELETE
    @Path("/{permissionId}")
    public Response deletePermission(@PathParam("permissionId") int permissionId) {

        Response response = null;

        /*
         * Disassociate the permission from all roles and delete it from the system.
         */
        Either<IException, Boolean> permissionWasDeletedEither = permissionBusiness.deletePermission(permissionId);

        if (permissionWasDeletedEither.isRight()) {

            response = Response.status(200).build();

        } else {

            IException exception = permissionWasDeletedEither.left().value();
            response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(exception).build();
        }

        return response;
    }
}