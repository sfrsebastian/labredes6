package gp.e3.autheo.authentication.domain.business;

import fj.data.Either;
import gp.e3.autheo.authentication.domain.entities.TenantInfo;
import gp.e3.autheo.authentication.domain.entities.User;
import gp.e3.autheo.authentication.infrastructure.exceptions.ExceptionFactory;
import gp.e3.autheo.authentication.infrastructure.exceptions.IException;
import gp.e3.autheo.authentication.infrastructure.exceptions.TechnicalException;
import gp.e3.autheo.authentication.persistence.daos.BusinessRolesDAO;
import gp.e3.autheo.authentication.persistence.daos.OrganizationDAO;
import gp.e3.autheo.authentication.persistence.daos.SystemBusinessRolesDAO;
import gp.e3.autheo.authentication.persistence.daos.UserDAO;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrganizationBusiness {

    private final OrganizationDAO organizationDAO;

    private final BasicDataSource dataSource;

    private final UserDAO userDao;

    private final BusinessRolesDAO businessRolesDao;

    private final SystemBusinessRolesDAO systemBusinessRolesDAO;

    /**
     * An object used to log.
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(UserBusiness.class);

    public OrganizationBusiness (OrganizationDAO organizationDAO, BasicDataSource dataSource, UserDAO userDao, BusinessRolesDAO businessRolesDao, SystemBusinessRolesDAO systemBusinessRolesDAO){

        this.organizationDAO = organizationDAO;
        this.dataSource = dataSource;
        this.userDao = userDao;
        this.businessRolesDao = businessRolesDao;
        this.systemBusinessRolesDAO = systemBusinessRolesDAO;
    }

    public Either<IException, Boolean> createTenant(TenantInfo tenantInfo, byte[] photo) {

        Either<IException, Boolean> either = null;

        try (Connection dbConnection = dataSource.getConnection()) {

            dbConnection.setAutoCommit(false);
            String originalPassword = tenantInfo.getPassword();
            String passwordHash = PasswordHandler.getPasswordHash(originalPassword);
            String passwordSalt = PasswordHandler.getSaltFromHashedAndSaltedPassword(passwordHash);

            Either<IException, Boolean> createOrgEither = null;

            createOrgEither = organizationDAO.createOrganization(dbConnection, tenantInfo.getOrganizationId());
            
            if (createOrgEither.isRight()){

                Either<IException, Boolean> businessRolesDAOEither = null;

                businessRolesDAOEither = businessRolesDao.createBusinessRole(dbConnection, "admin", tenantInfo.getOrganizationId());
                
                if (businessRolesDAOEither.isRight()) {
                	
                	Either<IException, Integer> systemBusinessRolesDAOEither = null;
                	
                	systemBusinessRolesDAOEither = systemBusinessRolesDAO.createSystemBusinessRole(dbConnection, "admin", tenantInfo.getOrganizationId());
                	
                	if ( systemBusinessRolesDAOEither.isRight() ) {
                		
                		
                		Either<IException, String> systemAdminRoleIdEither = systemBusinessRolesDAO.getSystemAdminRoleId(dbConnection, "admin", tenantInfo.getOrganizationId());
                		
                		if (systemAdminRoleIdEither.isRight()){
                			
                			String roleId = systemAdminRoleIdEither.right().value();

                			User user = new User(tenantInfo.getName(), tenantInfo.getDocumentType(), tenantInfo.getDocumentNumber(), tenantInfo.getEmail(), tenantInfo.getUsername(), 
                					tenantInfo.getTelephone(), tenantInfo.getAddress(), tenantInfo.getPassword(), false, tenantInfo.getOrganizationId(), roleId, "admin", "admin");
                			
                			either = userDao.createUserSpecificRoleId(dbConnection, user, passwordHash, passwordSalt, Integer.parseInt(roleId), photo);
                			
                			if ( either.isRight()){
                				
                				dbConnection.commit();
                				dbConnection.close();
                				either = Either.right(true);
                				
                			} else { 
                				
                				IException exception = either.left().value();
                				LOGGER.error("OrganizationBusiness :: createTenant", exception);
                				either = Either.left( new TechnicalException("AUTHEO_ERROR_ON_CREATE_USER"));
                				
                			}
                			
                		} else {
                			
                			IException exception = systemAdminRoleIdEither.left().value();
            				LOGGER.error("OrganizationBusiness :: createTenant", exception);
            				either = Either.left( new TechnicalException("AUTHEO_ERROR_ON_CREATE_USER"));
                			
                		}
                	} else {

                        dbConnection.rollback();
                        IException exception = businessRolesDAOEither.left().value();
                        LOGGER.error("OrganizationBusiness :: createTenant", exception);
                        either = Either.left( new TechnicalException("AUTHEO_ERROR_ON_CREATE_USER_TO_ADMIN_SYSTEM_BUSINESS_ROLE"));
                    }
                	
                } else {

                    dbConnection.rollback();
                    IException exception = businessRolesDAOEither.left().value();
                    LOGGER.error("OrganizationBusiness :: createTenant", exception);
                    either = Either.left( new TechnicalException("AUTHEO_ERROR_ON_CREATE_ORGANIZATION"));
                }
            } else {
            	
            	 dbConnection.rollback();
            	 
                 IException exception = createOrgEither.left().value();
                 System.out.println("moduleTokenEither.left().value()1: "+createOrgEither.left().value().getErrorMessage());
                 LOGGER.error("OrganizationBusiness :: createTenant", exception);
                 either = Either.left( new TechnicalException("AUTHEO_ERROR_ON_CREATE_ORGANIZATION"));
            	
            }
            
        } catch (SQLException e) {
        	e.printStackTrace();
            either = Either.left(ExceptionFactory.getTechnicalException(e));

        } catch (NoSuchAlgorithmException e) {
        	e.printStackTrace();
            LOGGER.error("OrganizationBusiness :: createTenant :: NoSuchAlgorithmException", e);
            either = Either.left(ExceptionFactory.getTechnicalException(e));

        } catch (InvalidKeySpecException e) {
        	e.printStackTrace();
            LOGGER.error("OrganizationBusiness :: createTenant :: NoSuchAlgorithmException", e);
            either = Either.left(ExceptionFactory.getTechnicalException(e));

        } catch (IllegalArgumentException e) {
        	e.printStackTrace();
            LOGGER.error("OrganizationBusiness :: createTenant :: IllegalArgumentException", e);
            either = Either.left(ExceptionFactory.getTechnicalException(e));
        }
        return either;
    }
}
