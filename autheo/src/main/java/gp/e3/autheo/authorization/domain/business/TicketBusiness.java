package gp.e3.autheo.authorization.domain.business;

import fj.data.Either;
import gp.e3.autheo.authentication.domain.business.TokenBusiness;
import gp.e3.autheo.authentication.domain.entities.Token;
import gp.e3.autheo.authentication.infrastructure.exceptions.ExceptionUtils;
import gp.e3.autheo.authentication.infrastructure.exceptions.IException;
import gp.e3.autheo.authorization.domain.entities.Ticket;
import gp.e3.autheo.authorization.infrastructure.constants.EnumRoleConstants;
import gp.e3.autheo.authorization.infrastructure.dtos.PermissionTuple;

import java.util.List;

public class TicketBusiness {
	
	private final TokenBusiness tokenBusiness;
	private final RoleBusiness roleBusiness;
	
	public TicketBusiness(TokenBusiness tokenBusiness, RoleBusiness roleBusiness) {
		
		this.tokenBusiness = tokenBusiness;
		this.roleBusiness = roleBusiness;
	}

	public Either<IException, Token> getAPITokenByTokenValue(String tokenValue) {
		
		return tokenBusiness.getAPIToken(tokenValue);
	}
	
	private boolean permissionBelongsToUserRole(PermissionTuple requestedPermission, List<PermissionTuple> rolePermissionsTuples) {
		
		boolean found = false;
		
		for (int i = 0; i < rolePermissionsTuples.size() && !found; i++) {
			
			PermissionTuple currentPermission = rolePermissionsTuples.get(i);
			found = requestedPermission.areTheSamePermission(currentPermission);
		}
		
		return found;
	}
	
	public Either<IException, Boolean> userIsAuthorized(Ticket ticket) {
		
	    Either<IException, Boolean> isAuthorizedEither = null;
		Either<IException, Token> apiTokenEither = tokenBusiness.getAPIToken(ticket.getTokenValue());
		
		if (apiTokenEither.isRight()) {
		    
		    Token apiToken = apiTokenEither.right().value();
		    String roleName = apiToken.getSystemRole();
		    
		    if (roleBusiness.rolePermissionsAreInRedis(roleName)) {
	            
	            PermissionTuple requestedPermission = new PermissionTuple(ticket.getHttpVerb(), ticket.getRequestedUrl());
	            List<PermissionTuple> rolePermissionsTuples = roleBusiness.getRolePermissionsFromRedis(roleName);
	            boolean isAuthorized = permissionBelongsToUserRole(requestedPermission, rolePermissionsTuples);
	            isAuthorizedEither = Either.right(isAuthorized);
	            
	        } else {
	            
	            Either<IException, Boolean> permissionsWereAddedToRedisEither = roleBusiness.addRolePermissionsToRedis(roleName);
	            
                if (permissionsWereAddedToRedisEither.isRight()) {
	                
	                // Recursion
	                isAuthorizedEither = userIsAuthorized(ticket);
	                
	            } else {
	                
	                isAuthorizedEither = Either.left(ExceptionUtils.getIExceptionFromEither(permissionsWereAddedToRedisEither));
	            }
	        }
		    
		} else {
		    
		    isAuthorizedEither = Either.left(ExceptionUtils.getIExceptionFromEither(apiTokenEither));
		}
		
		return isAuthorizedEither;
	}

	public Either<IException, Boolean> isPublicPermission(String httpVerb, String requestedUrl) {
		
	    Either<IException, Boolean> isPublicPermissionEither = null;
		String publicRole = EnumRoleConstants.PUBLIC_ROLE.getRoleName();
		
		if (roleBusiness.rolePermissionsAreInRedis(publicRole)) {
			
			PermissionTuple requestedPermission = new PermissionTuple(httpVerb, requestedUrl);
			List<PermissionTuple> rolePermissionsTuples = roleBusiness.getRolePermissionsFromRedis(publicRole);
			boolean isPublicPermission = permissionBelongsToUserRole(requestedPermission, rolePermissionsTuples);
			isPublicPermissionEither = Either.right(isPublicPermission);
			
		} else {
			
			Either<IException, Boolean> permissionsWereAddedToRedis = roleBusiness.addRolePermissionsToRedis(publicRole);
			
            if (permissionsWereAddedToRedis.isRight()) {
				
				// Recursion
				isPublicPermissionEither = isPublicPermission(httpVerb, requestedUrl);
				
			} else {
			    
			    isPublicPermissionEither = Either.left(ExceptionUtils.getIExceptionFromEither(permissionsWereAddedToRedis));
			}
		}
		
		return isPublicPermissionEither;
	}
}